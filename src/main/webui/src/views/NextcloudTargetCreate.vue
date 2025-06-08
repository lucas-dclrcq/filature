<template>
  <div class="p-4">
    <Toast />
    <div class="card">
      <div class="flex align-items-center justify-content-between mb-4">
        <h1 class="text-3xl font-bold">Create Nextcloud Target</h1>
        <router-link to="/">
          <Button label="Back to Home" icon="pi pi-home" class="p-button-secondary" />
        </router-link>
      </div>

      <Card>
        <template #content>
          <form @submit.prevent="createTarget" class="p-fluid">
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
                label="Create Target" 
                icon="pi pi-check" 
                :loading="loading"
              />
            </div>
          </form>
        </template>
      </Card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useToast } from 'primevue/usetoast';
import {getFilatureAPI} from "../api/service/catalog.ts";

const router = useRouter();
const toast = useToast();
const api = getFilatureAPI()

const target = ref({
  url: '',
  username: '',
  password: ''
});

const loading = ref(false);
const submitted = ref(false);

const createTarget = async () => {
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

  loading.value = true;

  try {
    await api.postApiTargetsNextcloud({
      url: target.value.url,
      username: target.value.username,
      password: target.value.password
    });

    toast.add({
      severity: 'success',
      summary: 'Success',
      detail: 'Nextcloud target created successfully',
      life: 3000
    });

    await router.push('/targets');
  } catch (error) {
    console.error('Error creating Nextcloud target:', error);
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Failed to create Nextcloud target. Please try again.',
      life: 3000
    });
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
/* Additional styles if needed */
</style>
