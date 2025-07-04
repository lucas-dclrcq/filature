  <template>
  <div class="p-4">
    <Toast />
    <div class="card">
      <div class="flex align-items-center justify-content-between mb-4">
        <h1 class="text-3xl font-bold">Edit Free Source</h1>
        <router-link to="/sources">
          <Button label="Back to Sources" icon="pi pi-arrow-left" class="p-button-secondary" />
        </router-link>
      </div>
      
      <div v-if="loading" class="flex justify-content-center p-4">
        <i class="pi pi-spin pi-spinner" style="font-size: 2rem"></i>
      </div>
      
      <Card v-else>
        <template #content>
          <form @submit.prevent="updateSource" class="p-fluid">
            <div class="field mb-4">
              <label for="login" class="font-bold">Login</label>
              <InputText 
                id="login" 
                v-model="source.login" 
                placeholder="Enter Free login" 
                :class="{'p-invalid': submitted && !source.login}"
                required
              />
              <small v-if="submitted && !source.login" class="p-error">Login is required.</small>
            </div>
            
            <div class="field mb-4">
              <label for="password" class="font-bold">Password</label>
              <InputText 
                id="password" 
                type="password" 
                v-model="source.password" 
                placeholder="Enter Free password" 
                :class="{'p-invalid': submitted && !source.password}"
                required
              />
              <small v-if="submitted && !source.password" class="p-error">Password is required.</small>
            </div>
            
            <div class="flex justify-content-end">
              <Button 
                type="submit" 
                label="Update Source" 
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

const sourceId = ref<number>(parseInt(route.params.id as string));
const source = ref({
  login: '',
  password: ''
});

const loading = ref(true);
const submitting = ref(false);
const submitted = ref(false);

onMounted(async () => {
  if (!sourceId.value) {
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Source ID is missing',
      life: 3000
    });
    await router.push('/sources');
    return;
  }
  
  try {
    const response = await api.getApiSourcesFreeId(sourceId.value);
    if (response.data) {
      source.value.login = response.data.login || '';
      source.value.password = "********";
    }
  } catch (error) {
    console.error('Error loading source:', error);
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Failed to load source details. Please try again.',
      life: 3000
    });
    await router.push('/sources');
  } finally {
    loading.value = false;
  }
});

const updateSource = async () => {
  submitted.value = true;
  
  if (!source.value.login || !source.value.password) {
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
    await api.putApiSourcesFreeId(sourceId.value, {
      login: source.value.login,
      password: source.value.password
    });
    
    toast.add({
      severity: 'success',
      summary: 'Success',
      detail: 'Free source updated successfully',
      life: 3000
    });
    
    await router.push('/sources');
  } catch (error) {
    console.error('Error updating Free source:', error);
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Failed to update Free source. Please try again.',
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