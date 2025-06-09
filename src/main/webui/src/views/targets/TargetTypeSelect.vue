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
        <h2 class="text-xl mb-3">Select Target</h2>
        <div class="grid">
          <target-select-element v-for="target in targets" :title="target.title" :name="target.name" :description="target.description" :icon-path="target.iconPath"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref} from "vue";
import TargetSelectElement from "../../components/TargetSelectElement.vue";

const targets = ref([
  {
    title: "Nextcloud",
    name: "nextcloud",
    description: "Self-hosted cloud storage and collaboration platform",
    iconPath: "/icons/nextcloud.png"
  }
]);
</script>