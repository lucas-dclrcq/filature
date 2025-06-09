<template>
  <div class="p-4">
    <Toast />
    <div class="card">
      <div class="flex align-items-center justify-content-between mb-4">
        <h1 class="text-3xl font-bold">Edit Nextcloud Target</h1>
        <router-link to="/targets">
          <Button label="Back to Targets" icon="pi pi-arrow-left" class="p-button-secondary" />
        </router-link>
      </div>

      <div v-if="loading" class="flex justify-content-center p-4">
        <i class="pi pi-spin pi-spinner" style="font-size: 2rem"></i>
      </div>

      <Card v-else>
        <template #content>
          <form @submit.prevent="updateTarget" class="p-fluid">
            <div class="field mb-4">
              <label for="url" class="font-bold">Nextcloud URL</label>
              <InputText 
                id="url" 
                v-model="target.url" 
                placeholder="https://nextcloud.example.com" 
                :class="{'p-invalid': submitted && !target.url}"
                required
              />
              <small v-if="submitted && !target.url" class="p-error">URL is required.</small>
            </div>

            <div class="field mb-4">
              <label for="username" class="font-bold">Username</label>
              <InputText 
                id="username" 
                v-model="target.username" 
                placeholder="Enter username" 
                :class="{'p-invalid': submitted && !target.username}"
                required
              />
              <small v-if="submitted && !target.username" class="p-error">Username is required.</small>
            </div>

            <div class="field mb-4">
              <label for="password" class="font-bold">Password</label>
              <InputText 
                id="password" 
                type="password" 
                v-model="target.password" 
                placeholder="Enter password" 
                :class="{'p-invalid': submitted && !target.password}"
                required
              />
              <small v-if="submitted && !target.password" class="p-error">Password is required.</small>
            </div>

            <div class="flex justify-content-end">
              <Button 
                type="submit" 
                label="Update Target" 
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
import {getFilatureAPI} from "../../../api/service/catalog.ts";

const router = useRouter();
const route = useRoute();
const toast = useToast();
const api = getFilatureAPI();

const targetId = ref<number>(parseInt(route.params.id as string));
const target = ref({
  url: '',
  username: '',
  password: ''
});

const loading = ref(true);
const submitting = ref(false);
const submitted = ref(false);

onMounted(async () => {
  if (!targetId.value) {
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Target ID is missing',
      life: 3000
    });
    await router.push('/targets');
    return;
  }
  
  try {
    const response = await api.getApiTargetsNextcloudId(targetId.value);
    if (response.data) {
      target.value.url = response.data.url || '';
      target.value.username = response.data.username || '';
    }
  } catch (error) {
    console.error('Error loading target:', error);
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Failed to load target details. Please try again.',
      life: 3000
    });
    await router.push('/targets');
  } finally {
    loading.value = false;
  }
});

const updateTarget = async () => {
  submitted.value = true;

  if (!target.value.url || !target.value.username || !target.value.password) {
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
    await api.putApiTargetsNextcloudId(targetId.value, {
      url: target.value.url,
      username: target.value.username,
      password: target.value.password
    });

    toast.add({
      severity: 'success',
      summary: 'Success',
      detail: 'Nextcloud target updated successfully',
      life: 3000
    });

    await router.push('/targets');
  } catch (error) {
    console.error('Error updating Nextcloud target:', error);
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Failed to update Nextcloud target. Please try again.',
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