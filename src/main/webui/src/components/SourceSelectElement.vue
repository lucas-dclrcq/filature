<template>
  <div class="col-12 md:col-6 lg:col-4">
    <div class="source-type-card p-3" @click="navigateToCreate()">
      <div class="flex align-items-center">
        <img :src=iconPath alt="{{ title }}" class="mr-2" style="width: 32px; height: 32px;"/>
        <div>
          <h3 class="text-lg font-bold m-0">{{ title }}</h3>
          <p class="text-sm text-gray-600 m-0">{{ description }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {useRouter} from 'vue-router';

const router = useRouter();

const props = defineProps(['title', 'name', 'description', 'iconPath'])

const navigateToCreate = () => {
  router.push(`/sources/${props.name}/create`);
};
</script>

<style scoped>
.source-type-card {
  border: 1px solid #e9ecef;
  border-radius: 8px;
  transition: all 0.2s;
  cursor: pointer;
}

.source-type-card:hover {
  background-color: #f8f9fa;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
</style>