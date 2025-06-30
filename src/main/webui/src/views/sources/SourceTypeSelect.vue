<template>
  <div class="p-4">
    <Toast />
    <div class="card">
      <div class="flex align-items-center mb-4">
        <router-link to="/sources">
          <Button icon="pi pi-arrow-left" class="p-button-text" />
        </router-link>
        <h1 class="text-3xl font-bold">Create Source</h1>
      </div>

      <div class="p-fluid">
        <h2 class="text-xl mb-3">Select Source</h2>
        <div class="grid">
          <source-select-element v-for="source in sources" :title="source.title" :name="source.name" :description="source.description" :icon-path="source.iconPath"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">

import {ref} from "vue";
import SourceSelectElement from "../../components/SourceSelectElement.vue";

const sources = ref([
  {
    title: "Enercoop",
    name: "enercoop",
    description: "French cooperative utility company",
    iconPath: "/icons/enercoop.png"
  },
  {
    title: "Free",
    name: "free",
    description: "French telecommunications company",
    iconPath: "/icons/free.png"
  },
  {
    title: "Ileo",
    name: "ileo",
    description: "Water supplier",
    iconPath: "/icons/ileo.png"
  }
]);

</script>
