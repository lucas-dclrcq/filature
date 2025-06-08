<template>
  <div class="p-4">
    <Toast />
    <div class="card">
      <div class="flex align-items-center justify-content-between mb-4">
        <h1 class="text-3xl font-bold">Sources</h1>
        <div class="flex gap-2">
          <router-link to="/sources/create">
            <Button label="Create Source" icon="pi pi-plus" />
          </router-link>
        </div>
      </div>

      <div v-if="loading" class="flex justify-content-center p-4">
        <i class="pi pi-spin pi-spinner" style="font-size: 2rem"></i>
      </div>

      <div v-else-if="sources.length === 0" class="p-4 text-center">
        <p class="text-xl mb-4">No sources found.</p>
      </div>

      <DataTable v-else :value="sources" stripedRows class="p-datatable-sm">
        <Column field="id" header="ID"></Column>
        <Column header="Type">
          <template #body>
            <div class="flex align-items-center">
              <img src="/icons/enercoop.png" alt="Enercoop" style="width: 24px; height: 24px;" />
              <span class="ml-2">Enercoop</span>
            </div>
          </template>
        </Column>
        <Column field="name" header="Name"></Column>
        <Column header="Actions">
          <template #body="slotProps">
            <Button 
              icon="pi pi-pencil" 
              class="p-button-rounded p-button-warning mr-2" 
              @click="editSource(slotProps.data)" 
              tooltip="Edit"
            />
            <Button 
              icon="pi pi-trash" 
              class="p-button-rounded p-button-danger" 
              @click="confirmDelete(slotProps.data)" 
              tooltip="Delete"
            />
          </template>
        </Column>
      </DataTable>

      <Dialog 
        v-model:visible="deleteDialog" 
        header="Confirm Delete" 
        :style="{ width: '450px' }" 
        :modal="true"
      >
        <div class="confirmation-content">
          <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
          <span v-if="sourceToDelete">
            Are you sure you want to delete the source <b>{{ sourceToDelete.name || 'Unknown' }}</b>?
          </span>
        </div>
        <template #footer>
          <Button 
            label="No" 
            icon="pi pi-times" 
            class="p-button-text" 
            @click="deleteDialog = false" 
          />
          <Button 
            label="Yes" 
            icon="pi pi-check" 
            class="p-button-danger" 
            @click="deleteSource" 
            :loading="deleteLoading"
          />
        </template>
      </Dialog>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useToast } from 'primevue/usetoast';
import type { SourceSummary } from '../api/model';
import { getFilatureAPI } from "../api/service/catalog.ts";
import {useRouter} from "vue-router";

const router = useRouter();

const toast = useToast();
const api = getFilatureAPI();

const sources = ref<SourceSummary[]>([]);
const loading = ref(true);
const deleteDialog = ref(false);
const sourceToDelete = ref<SourceSummary | null>(null);
const deleteLoading = ref(false);

onMounted(async () => {
  await loadSources();
});

const loadSources = async () => {
  loading.value = true;
  try {
    const response = await api.getApiSources();
    sources.value = response.data || [];
  } catch (error) {
    console.error('Error loading sources:', error);
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Failed to load sources. Please try again.',
      life: 3000
    });
  } finally {
    loading.value = false;
  }
};

const confirmDelete = (source: SourceSummary) => {
  sourceToDelete.value = source;
  deleteDialog.value = true;
};

const deleteSource = async () => {
  if (!sourceToDelete.value?.id) return;

  deleteLoading.value = true;

  try {
    await api.deleteApiSourcesEnercoopId(sourceToDelete.value.id);

    toast.add({
      severity: 'success',
      summary: 'Success',
      detail: 'Source deleted successfully',
      life: 3000
    });

    deleteDialog.value = false;
    await loadSources();
  } catch (error) {
    console.error('Error deleting source:', error);
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Failed to delete source. Please try again.',
      life: 3000
    });
  } finally {
    deleteLoading.value = false;
  }
};

const editSource = (source: SourceSummary) => {
  console.log('Edit source:', source);
  router.push(`/sources/enercoop/edit/${source.id}`);
};
</script>

<style scoped>
.confirmation-content {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
