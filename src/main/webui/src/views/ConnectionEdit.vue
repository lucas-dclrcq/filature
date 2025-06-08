<template>
  <div class="p-4">
    <Toast />
    <div class="card">
      <div class="flex align-items-center justify-content-between mb-4">
        <h1 class="text-3xl font-bold">Edit Connection</h1>
        <router-link to="/connections">
          <Button label="Back to Connections" icon="pi pi-arrow-left" class="p-button-secondary" />
        </router-link>
      </div>

      <div v-if="loading" class="flex justify-content-center p-4">
        <i class="pi pi-spin pi-spinner" style="font-size: 2rem"></i>
      </div>

      <Card v-else>
        <template #content>
          <div v-if="!sourcesLoaded || !targetsLoaded" class="p-4 text-center">
            <p v-if="!sourcesLoaded && !targetsLoaded" class="text-xl">
              No sources or targets found. Please create them first.
            </p>
            <p v-else-if="!sourcesLoaded" class="text-xl">
              No Enercoop sources found. Please create a source first.
            </p>
            <p v-else class="text-xl">
              No Nextcloud targets found. Please create a target first.
            </p>

            <div class="flex justify-content-center gap-2 mt-4">
              <router-link to="/sources/create" v-if="!sourcesLoaded">
                <Button label="Create Source" icon="pi pi-plus" />
              </router-link>
              <router-link to="/targets/create" v-if="!targetsLoaded">
                <Button label="Create Target" icon="pi pi-plus" />
              </router-link>
            </div>
          </div>

          <form v-else @submit.prevent="updateConnection" class="p-fluid">
            <div class="field mb-4">
              <label for="source" class="font-bold flex align-items-center">
                <img src="/icons/enercoop.png" alt="Enercoop" class="mr-2" style="width: 24px; height: 24px;" />
                Enercoop Source
              </label>
              <select 
                id="source" 
                v-model="connection.sourceConfigurationId" 
                class="p-inputtext w-full"
                :class="{'p-invalid': submitted && !connection.sourceConfigurationId}"
                required
              >
                <option value="" disabled>Select a source</option>
                <option v-for="source in sources" :key="source.id" :value="source.id">
                  {{ source.name }}
                </option>
              </select>
              <small v-if="submitted && !connection.sourceConfigurationId" class="p-error">Source is required.</small>
            </div>

            <div class="field mb-4">
              <label for="target" class="font-bold flex align-items-center">
                <img src="/icons/nextcloud.png" alt="Nextcloud" class="mr-2" style="width: 24px; height: 24px;" />
                Nextcloud Target
              </label>
              <select 
                id="target" 
                v-model="connection.targetConfigurationId" 
                class="p-inputtext w-full"
                :class="{'p-invalid': submitted && !connection.targetConfigurationId}"
                required
              >
                <option value="" disabled>Select a target</option>
                <option v-for="target in targets" :key="target.id" :value="target.id">
                  {{ target.name }}
                </option>
              </select>
              <small v-if="submitted && !connection.targetConfigurationId" class="p-error">Target is required.</small>
            </div>

            <div class="field mb-4">
              <label for="targetUploadPath" class="font-bold">Target Upload Path</label>
              <InputText 
                id="targetUploadPath" 
                v-model="connection.targetUploadPath" 
                placeholder="/path/to/upload" 
                :class="{'p-invalid': submitted && !connection.targetUploadPath}"
                required
              />
              <small v-if="submitted && !connection.targetUploadPath" class="p-error">Upload path is required.</small>
            </div>

            <div class="flex justify-content-end">
              <Button 
                type="submit" 
                label="Update Connection" 
                icon="pi pi-check" 
                :loading="submitting"
              />
            </div>
          </form>
        </template>
      </Card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useToast } from 'primevue/usetoast';
import type { SourceSummary, TargetSummary } from '../api/model';
import { getFilatureAPI } from "../api/service/catalog.ts";

const router = useRouter();
const route = useRoute();
const toast = useToast();
const api = getFilatureAPI();

const connectionId = ref<number>(parseInt(route.params.id as string));
const sources = ref<SourceSummary[]>([]);
const targets = ref<TargetSummary[]>([]);
const sourcesLoaded = ref(false);
const targetsLoaded = ref(false);
const loading = ref(true);
const submitting = ref(false);
const submitted = ref(false);

const connection = ref({
  sourceConfigurationId: '',
  targetConfigurationId: '',
  targetUploadPath: ''
});

onMounted(async () => {
  if (!connectionId.value) {
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Connection ID is missing',
      life: 3000
    });
    router.push('/connections');
    return;
  }

  try {
    // Load connection details
    const connectionResponse = await api.getApiConnectionsId(connectionId.value);
    if (connectionResponse.data) {
      const connectionData = connectionResponse.data;
      connection.value.sourceConfigurationId = connectionData.source?.id?.toString() || '';
      connection.value.targetConfigurationId = connectionData.target?.id?.toString() || '';
      connection.value.targetUploadPath = connectionData.targetUploadPath || '';
    }

    // Load sources
    const sourcesResponse = await api.getApiSources();
    if (sourcesResponse.data && Array.isArray(sourcesResponse.data)) {
      sources.value = sourcesResponse.data;
      sourcesLoaded.value = sources.value.length > 0;
    }

    // Load targets
    const targetsResponse = await api.getApiTargets();
    if (targetsResponse.data && Array.isArray(targetsResponse.data)) {
      targets.value = targetsResponse.data;
      targetsLoaded.value = targets.value.length > 0;
    }
  } catch (error) {
    console.error('Error loading data:', error);
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Failed to load connection details. Please try again.',
      life: 3000
    });
    router.push('/connections');
  } finally {
    loading.value = false;
  }
});

const updateConnection = async () => {
  submitted.value = true;

  // Validate all fields are filled
  if (!connection.value.sourceConfigurationId || !connection.value.targetConfigurationId || !connection.value.targetUploadPath) {
    toast.add({
      severity: 'error',
      summary: 'Validation Error',
      detail: 'Please fill in all required fields',
      life: 3000
    });
    return;
  }

  submitting.value = true;

  try {
    // Since there's no update API for connections, we'll delete the existing one and create a new one
    // First, delete the existing connection
    await api.deleteApiConnectionsId(connectionId.value);

    // Then create a new connection with the updated data
    await api.postApiConnections({
      sourceConfigurationId: Number(connection.value.sourceConfigurationId),
      targetConfigurationId: Number(connection.value.targetConfigurationId),
      targetUploadPath: connection.value.targetUploadPath
    });

    toast.add({
      severity: 'success',
      summary: 'Success',
      detail: 'Connection updated successfully',
      life: 3000
    });

    await router.push('/connections');
  } catch (error) {
    console.error('Error updating connection:', error);
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Failed to update connection. Please try again.',
      life: 3000
    });
  } finally {
    submitting.value = false;
  }
};
</script>

<style scoped>
/* Additional styles if needed */
</style>