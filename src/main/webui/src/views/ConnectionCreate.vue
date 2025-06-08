<template>
  <div class="p-4">
    <Toast />
    <div class="card">
      <div class="flex align-items-center justify-content-between mb-4">
        <h1 class="text-3xl font-bold">Create Connection</h1>
        <router-link to="/">
          <Button label="Back to Home" icon="pi pi-home" class="p-button-secondary" />
        </router-link>
      </div>

      <Card>
        <template #content>
          <div v-if="loading" class="flex justify-content-center">
            <i class="pi pi-spin pi-spinner" style="font-size: 2rem"></i>
          </div>

          <div v-else-if="!sourcesLoaded || !targetsLoaded" class="p-4 text-center">
            <p v-if="!sourcesLoaded && !targetsLoaded" class="text-xl">
              No sources or targets found. Please create them first.
            </p>
            <p v-else-if="!sourcesLoaded" class="text-xl">
              No source found. Please create a source first.
            </p>
            <p v-else class="text-xl">
              No target found. Please create a target first.
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

          <form v-else @submit.prevent="createConnection" class="p-fluid">
            <div class="field mb-4">
              <label for="source" class="font-bold flex align-items-center">
                Source
              </label>
              <select 
                id="source" 
                v-model="connection.sourceConfigurationId" 
                class="p-inputtext w-full"
                :class="{'p-invalid': submitted && !connection.sourceConfigurationId}"
                required
              >
                <option value="" disabled selected>Select a source</option>
                <option v-for="source in sources" :key="source.id" :value="source.id">
                  {{ source.name }}
                </option>
              </select>
              <small v-if="submitted && !connection.sourceConfigurationId" class="p-error">Source is required.</small>
            </div>

            <div class="field mb-4">
              <label for="target" class="font-bold flex align-items-center">
                Target
              </label>
              <select 
                id="target" 
                v-model="connection.targetConfigurationId" 
                class="p-inputtext w-full"
                :class="{'p-invalid': submitted && !connection.targetConfigurationId}"
                required
              >
                <option value="" disabled selected>Select a target</option>
                <option v-for="target in targets" :key="target.id" :value="target.id">
                  {{ target.name }}
                </option>
              </select>
              <small v-if="submitted && !connection.targetConfigurationId" class="p-error">Target is required.</small>
            </div>

            <div class="field mb-4">
              <label for="targetUploadPath" class="font-bold">Upload Path</label>
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
                label="Create Connection" 
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
import { useRouter } from 'vue-router';
import { useToast } from 'primevue/usetoast';
import type { SourceSummary, TargetSummary } from '../api/model';
import {getFilatureAPI} from "../api/service/catalog.ts";

const router = useRouter();
const toast = useToast();
const api = getFilatureAPI()

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
  try {
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
    console.error('Error loading sources and targets:', error);
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Failed to load sources and targets. Please try again.',
      life: 3000
    });
  } finally {
    loading.value = false;
  }
});

const createConnection = async () => {
  submitted.value = true;

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
    await api.postApiConnections({
      sourceConfigurationId: Number(connection.value.sourceConfigurationId),
      targetConfigurationId: Number(connection.value.targetConfigurationId),
      targetUploadPath: connection.value.targetUploadPath
    });

    toast.add({
      severity: 'success',
      summary: 'Success',
      detail: 'Connection created successfully',
      life: 3000
    });

    await router.push('/connections');
  } catch (error) {
    console.error('Error creating connection:', error);
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Failed to create connection. Please try again.',
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
