<template>
  <div class="p-4">
    <Toast />
    <div class="card">
      <div class="flex align-items-center mb-4">
        <router-link to="/targets">
          <Button icon="pi pi-arrow-left" class="p-button-text" />
        </router-link>
        <h1 class="text-3xl font-bold">Create Target</h1>
      </div>

      <div class="p-fluid">
        <h2 class="text-xl mb-3">Select Target Type</h2>
        <div class="grid">
          <div class="col-12 md:col-6 lg:col-4">
            <div class="target-type-card p-3" @click="navigateToCreate('nextcloud')">
              <div class="flex align-items-center">
                <img src="/icons/nextcloud.png" alt="Nextcloud" class="mr-2" style="width: 32px; height: 32px;" />
                <div>
                  <h3 class="text-lg font-bold m-0">Nextcloud</h3>
                  <p class="text-sm text-gray-600 m-0">Self-hosted cloud storage and collaboration platform</p>
                </div>
              </div>
            </div>
          </div>
          <!-- Additional target types can be added here in the future -->
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter();

const navigateToCreate = (type: string) => {
  router.push(`/targets/${type}/create`);
};
</script>

<style scoped>
.target-type-card {
  border: 1px solid #e9ecef;
  border-radius: 8px;
  transition: all 0.2s;
  cursor: pointer;
}

.target-type-card:hover {
  background-color: #f8f9fa;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
</style>
