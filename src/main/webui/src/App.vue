<template>
  <div class="app-container">
    <header class="app-header">
      <div class="container header-container">
        <div class="flex align-items-center justify-content-between w-full">
          <router-link to="/" class="no-underline">
            <h1 class="text-2xl font-bold text-white m-0">Filature</h1>
          </router-link>
          <div class="flex align-items-center">
            <Button icon="pi pi-user" class="p-button-rounded p-button-text p-button-plain text-white" aria-label="Profile" @click="showUserMenu" />
            <Menu ref="userMenuRef" :model="[{label: 'Logout', icon: 'pi pi-sign-out', command: logout}]" :popup="true" />
          </div>
        </div>
      </div>
    </header>

    <div class="app-main">
      <aside class="app-sidebar">
        <nav>
          <ul class="sidebar-nav">
            <li>
              <router-link to="/" class="sidebar-link">
                <i class="pi pi-link mr-2"></i>
                Connections
              </router-link>
            </li>
            <li>
              <router-link to="/synchronizations" class="sidebar-link">
                <i class="pi pi-sync mr-2"></i>
                Synchronizations
              </router-link>
            </li>
            <li>
              <router-link to="/sources" class="sidebar-link">
                <i class="pi pi-database mr-2"></i>
                Sources
              </router-link>
            </li>
            <li>
              <router-link to="/targets" class="sidebar-link">
                <i class="pi pi-cloud mr-2"></i>
                Targets
              </router-link>
            </li>
            <li>
              <router-link to="/notifiers" class="sidebar-link">
                <i class="pi pi-bell mr-2"></i>
                Notifiers
              </router-link>
            </li>
          </ul>
        </nav>
      </aside>

      <main class="app-content">
        <div class="content-container">
          <router-view />
        </div>
      </main>
    </div>

    <footer class="app-footer">
      <div class="container">
        <p class="text-center text-sm text-gray-500 m-0">
          Filature - Data Synchronization Tool
        </p>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const userMenuRef = ref();
const showUserMenu = (event: Event) => {
  userMenuRef.value.toggle(event);
};

const logout = () => {
  window.location.href = '/logout';
};
</script>

<style>
.app-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.app-header {
  background-color: #1976d2;
  padding: 1rem 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  z-index: 10;
}

.app-main {
  display: flex;
  flex: 1;
}

.app-sidebar {
  width: 250px;
  background-color: #f8f9fa;
  border-right: 1px solid #e9ecef;
  padding: 1.5rem 0;
  box-shadow: 1px 0 3px rgba(0, 0, 0, 0.05);
}

.sidebar-nav {
  list-style: none;
  padding: 0;
  margin: 0;
}

.sidebar-link {
  display: flex;
  align-items: center;
  padding: 0.75rem 1.5rem;
  color: #495057;
  text-decoration: none;
  font-weight: 500;
  transition: background-color 0.2s, color 0.2s;
}

.sidebar-link:hover, .sidebar-link.router-link-active {
  background-color: #e9ecef;
  color: #1976d2;
}

.app-content {
  flex: 1;
  padding: 2rem;
  background-color: #f8f9fa;
}

.content-container {
  max-width: 1200px;
  margin: 0 auto;
}

.app-footer {
  padding: 1rem 0;
  background-color: #f8f9fa;
  border-top: 1px solid #e9ecef;
}

.container {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
}

.header-container {
  max-width: 100%;
  padding: 0 2rem;
}

/* Override the default text-align: center from style.css */
#app {
  text-align: left;
  max-width: none;
  margin: 0;
  padding: 0;
}
</style>
