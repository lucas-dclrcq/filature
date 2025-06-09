<template>
  <div class="p-4">
    <Toast />
    <div class="card">
      <div class="flex align-items-center mb-4">
        <router-link to="/sources/create">
          <Button icon="pi pi-arrow-left" class="p-button-text" />
        </router-link>
        <h1 class="text-3xl font-bold">Create Enercoop Source</h1>
      </div>
      
      <Card>
        <template #content>
          <form @submit.prevent="createSource" class="p-fluid">
            <div class="field mb-4">
              <label for="username" class="font-bold">Username</label>
              <InputText 
                id="username" 
                v-model="source.username" 
                placeholder="Enter Enercoop username" 
                :class="{'p-invalid': submitted && !source.username}"
                required
              />
              <small v-if="submitted && !source.username" class="p-error">Username is required.</small>
            </div>
            
            <div class="field mb-4">
              <label for="password" class="font-bold">Password</label>
              <InputText 
                id="password" 
                type="password" 
                v-model="source.password" 
                placeholder="Enter Enercoop password" 
                :class="{'p-invalid': submitted && !source.password}"
                required
              />
              <small v-if="submitted && !source.password" class="p-error">Password is required.</small>
            </div>
            
            <div class="flex justify-content-end">
              <Button 
                type="submit" 
                label="Create Source" 
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
import {getFilatureAPI} from "../../../api/service/catalog.ts";

const router = useRouter();
const toast = useToast();
const api = getFilatureAPI()

const source = ref({
  username: '',
  password: ''
});

const loading = ref(false);
const submitted = ref(false);

const createSource = async () => {
  submitted.value = true;

  if (!source.value.username || !source.value.password) {
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
    await api.postApiSourcesEnercoop({
      username: source.value.username,
      password: source.value.password
    });
    
    toast.add({
      severity: 'success',
      summary: 'Success',
      detail: 'Enercoop source created successfully',
      life: 3000
    });
    
    await router.push('/sources');
  } catch (error) {
    console.error('Error creating Enercoop source:', error);
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Failed to create Enercoop source. Please try again.',
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