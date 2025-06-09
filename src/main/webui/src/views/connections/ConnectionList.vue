<template>
  <div class="p-4">
    <Toast />
    <div class="card">
      <div class="flex align-items-center justify-content-between mb-4">
        <h1 class="text-3xl font-bold">Connections</h1>
        <div class="flex gap-2">
          <router-link to="/connections/create">
            <Button label="Create Connection" icon="pi pi-plus" />
          </router-link>
        </div>
      </div>

      <div v-if="loading" class="flex justify-content-center p-4">
        <i class="pi pi-spin pi-spinner" style="font-size: 2rem"></i>
      </div>

      <div v-else-if="connections.length === 0" class="p-4 text-center">
        <p class="text-xl mb-4">No connections found.</p>
      </div>

      <DataTable v-else :value="connections" stripedRows class="p-datatable-sm">
        <Column field="id" header="ID"></Column>
        <Column header="Source">
          <template #body="slotProps">
            {{ slotProps.data.source?.name || 'Unknown' }}
          </template>
        </Column>
        <Column header="Target">
          <template #body="slotProps">
            {{ slotProps.data.target?.name || 'Unknown' }}
          </template>
        </Column>
        <Column field="targetUploadPath" header="Upload Path"></Column>
        <Column header="Actions">
          <template #body="slotProps">
            <Button 
              icon="pi pi-sync" 
              class="p-button-rounded p-button-success mr-2" 
              @click="synchronizeConnection(slotProps.data.id)" 
              :loading="syncLoading[slotProps.data.id]"
              :disabled="syncLoading[slotProps.data.id]"
              tooltip="Synchronize"
            />
            <Button 
              icon="pi pi-pencil" 
              class="p-button-rounded p-button-warning mr-2" 
              @click="editConnection(slotProps.data)" 
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
          <span v-if="connectionToDelete">
            Are you sure you want to delete the connection between 
            <b>{{ connectionToDelete.source?.name || 'Unknown' }}</b> and 
            <b>{{ connectionToDelete.target?.name || 'Unknown' }}</b>?
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
            @click="deleteConnection" 
            :loading="deleteLoading"
          />
        </template>
      </Dialog>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue';
import { useToast } from 'primevue/usetoast';
import {useRouter} from "vue-router";
import type {ConnectionSummary} from "../../api/model";
import {getFilatureAPI} from "../../api/service/catalog.ts";

const router = useRouter();
const toast = useToast();
const api = getFilatureAPI();

const connections = ref<ConnectionSummary[]>([]);
const loading = ref(true);
const syncLoading = reactive<Record<number, boolean>>({});
const deleteDialog = ref(false);
const connectionToDelete = ref<ConnectionSummary | null>(null);
const deleteLoading = ref(false);

onMounted(async () => {
  await loadConnections();
});

const loadConnections = async () => {
  loading.value = true;
  try {
    const response = await api.getApiConnections();
    connections.value = response.data || [];
  } catch (error) {
    console.error('Error loading connections:', error);
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Failed to load connections. Please try again.',
      life: 3000
    });
  } finally {
    loading.value = false;
  }
};

const synchronizeConnection = async (connectionId: number | undefined) => {
  if (!connectionId) return;

  syncLoading[connectionId] = true;

  try {
    await api.postApiConnectionsConnectionIdSynchronize(connectionId);

    toast.add({
      severity: 'success',
      summary: 'Success',
      detail: 'Synchronization started successfully',
      life: 3000
    });
  } catch (error) {
    console.error('Error starting synchronization:', error);
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Failed to start synchronization. Please try again.',
      life: 3000
    });
  } finally {
    syncLoading[connectionId] = false;
  }
};

const confirmDelete = (connection: ConnectionSummary) => {
  connectionToDelete.value = connection;
  deleteDialog.value = true;
};

const deleteConnection = async () => {
  if (!connectionToDelete.value?.id) return;

  deleteLoading.value = true;

  try {
    await api.deleteApiConnectionsId(connectionToDelete.value.id);

    toast.add({
      severity: 'success',
      summary: 'Success',
      detail: 'Connection deleted successfully',
      life: 3000
    });

    deleteDialog.value = false;
    await loadConnections();
  } catch (error) {
    console.error('Error deleting connection:', error);
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Failed to delete connection. Please try again.',
      life: 3000
    });
  } finally {
    deleteLoading.value = false;
  }
};

const editConnection = (connection: ConnectionSummary) => {
  console.log('Edit connection:', connection);
  router.push(`/connections/edit/${connection.id}`);
};
</script>

<style scoped>
.confirmation-content {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
