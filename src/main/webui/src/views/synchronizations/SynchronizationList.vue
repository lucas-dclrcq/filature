<template>
  <div class="p-4">
    <Toast />
    <div class="card">
      <div class="flex align-items-center justify-content-between mb-4">
        <h1 class="text-3xl font-bold">Synchronizations</h1>
      </div>

      <div v-if="loading" class="flex justify-content-center p-4">
        <i class="pi pi-spin pi-spinner" style="font-size: 2rem"></i>
      </div>

      <div v-else-if="synchronizations.length === 0" class="p-4 text-center">
        <p class="text-xl mb-4">No synchronizations found.</p>
      </div>

      <DataTable v-else :value="synchronizations" stripedRows class="p-datatable-sm">
        <Column field="id" header="ID"></Column>
        <Column field="connectionId" header="Connection ID"></Column>
        <Column header="Status">
          <template #body="slotProps">
            <span :class="getStatusClass(slotProps.data.status)">
              {{ slotProps.data.status }}
            </span>
          </template>
        </Column>
        <Column field="error" header="Error">
          <template #body="slotProps">
            <span v-if="slotProps.data.error" class="text-red-500">
              {{ slotProps.data.error }}
            </span>
            <span v-else>-</span>
          </template>
        </Column>
        <Column field="startedAt" header="Started At">
          <template #body="slotProps">
            {{ formatDateTime(slotProps.data.startedAt) }}
          </template>
        </Column>
        <Column field="endedAt" header="Ended At">
          <template #body="slotProps">
            {{ formatDateTime(slotProps.data.endedAt) }}
          </template>
        </Column>
      </DataTable>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useToast } from 'primevue/usetoast';
import {getFilatureAPI} from "../../api/service/catalog.ts";
import type {SynchronizationSummary} from "../../api/model";


const toast = useToast();
const api = getFilatureAPI();

const synchronizations = ref<SynchronizationSummary[]>([]);
const loading = ref(true);

onMounted(async () => {
  await loadSynchronizations();
});

const loadSynchronizations = async () => {
  loading.value = true;
  try {
    const response = await api.getApiSynchronizations();
    synchronizations.value = response.data || [];
  } catch (error) {
    console.error('Error loading synchronizations:', error);
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Failed to load synchronizations. Please try again.',
      life: 3000
    });
  } finally {
    loading.value = false;
  }
};

const getStatusClass = (status: string) => {
  if (status === 'SUCCESS') {
    return 'text-green-500 font-bold';
  } else if (status === 'FAILURE') {
    return 'text-red-500 font-bold';
  }
  return '';
};

const formatDateTime = (dateTime: string | null) => {
  if (!dateTime) return '-';
  return new Date(dateTime).toLocaleString();
};
</script>

<style scoped>
/* No additional styles needed */
</style>