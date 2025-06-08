<template>
  <div class="p-4">
    <Toast />
    <div class="card">
      <div class="flex align-items-center justify-content-between mb-4">
        <h1 class="text-3xl font-bold">Targets</h1>
        <div class="flex gap-2">
          <router-link to="/targets/create">
            <Button label="Create Target" icon="pi pi-plus" />
          </router-link>
        </div>
      </div>

      <div v-if="loading" class="flex justify-content-center p-4">
        <i class="pi pi-spin pi-spinner" style="font-size: 2rem"></i>
      </div>

      <div v-else-if="targets.length === 0" class="p-4 text-center">
        <p class="text-xl mb-4">No targets found.</p>
      </div>

      <DataTable v-else :value="targets" stripedRows class="p-datatable-sm">
        <Column field="id" header="ID"></Column>
        <Column header="Type">
          <template #body>
            <div class="flex align-items-center">
              <img src="/icons/nextcloud.png" alt="Nextcloud" style="width: 24px; height: 24px;" />
              <span class="ml-2">Nextcloud</span>
            </div>
          </template>
        </Column>
        <Column field="name" header="Name"></Column>
        <Column header="Actions">
          <template #body="slotProps">
            <Button 
              icon="pi pi-pencil" 
              class="p-button-rounded p-button-warning mr-2" 
              @click="editTarget(slotProps.data)" 
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
          <span v-if="targetToDelete">
            Are you sure you want to delete the target <b>{{ targetToDelete.name || 'Unknown' }}</b>?
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
            @click="deleteTarget" 
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
import type { TargetSummary } from '../api/model';
import { getFilatureAPI } from "../api/service/catalog.ts";
import {useRouter} from "vue-router";

const router = useRouter();
const toast = useToast();
const api = getFilatureAPI();

const targets = ref<TargetSummary[]>([]);
const loading = ref(true);
const deleteDialog = ref(false);
const targetToDelete = ref<TargetSummary | null>(null);
const deleteLoading = ref(false);

onMounted(async () => {
  await loadTargets();
});

const loadTargets = async () => {
  loading.value = true;
  try {
    const response = await api.getApiTargets();
    targets.value = response.data || [];
  } catch (error) {
    console.error('Error loading targets:', error);
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Failed to load targets. Please try again.',
      life: 3000
    });
  } finally {
    loading.value = false;
  }
};

const confirmDelete = (target: TargetSummary) => {
  targetToDelete.value = target;
  deleteDialog.value = true;
};

const deleteTarget = async () => {
  if (!targetToDelete.value?.id) return;

  deleteLoading.value = true;

  try {
    // Assuming there's a delete endpoint for targets
    await api.deleteApiTargetsNextcloudId(targetToDelete.value.id);

    toast.add({
      severity: 'success',
      summary: 'Success',
      detail: 'Target deleted successfully',
      life: 3000
    });

    deleteDialog.value = false;
    await loadTargets();
  } catch (error) {
    console.error('Error deleting target:', error);
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Failed to delete target. Please try again.',
      life: 3000
    });
  } finally {
    deleteLoading.value = false;
  }
};

const editTarget = (target: TargetSummary) => {
  console.log('Edit target:', target);
  router.push(`/targets/nextcloud/edit/${target.id}`);
};
</script>

<style scoped>
.confirmation-content {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
