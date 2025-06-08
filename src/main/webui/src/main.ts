import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import PrimeVue from 'primevue/config'
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import Card from 'primevue/card'
import Toast from 'primevue/toast'
import ToastService from 'primevue/toastservice'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Dialog from 'primevue/dialog'

import 'primevue/resources/themes/lara-light-blue/theme.css'
import 'primevue/resources/primevue.min.css'
import 'primeicons/primeicons.css'
import 'primeflex/primeflex.css'
import './style.css'

import App from './App.vue'
import NextcloudTargetCreate from './views/NextcloudTargetCreate.vue'
import NextcloudTargetEdit from './views/NextcloudTargetEdit.vue'
import EnercoopSourceCreate from './views/EnercoopSourceCreate.vue'
import EnercoopSourceEdit from './views/EnercoopSourceEdit.vue'
import ConnectionCreate from './views/ConnectionCreate.vue'
import ConnectionEdit from './views/ConnectionEdit.vue'
import ConnectionList from './views/ConnectionList.vue'
import SourcesList from './views/SourcesList.vue'
import TargetsList from './views/TargetsList.vue'
import SourceTypeSelect from './views/SourceTypeSelect.vue'
import TargetTypeSelect from './views/TargetTypeSelect.vue'
import SynchronizationList from './views/SynchronizationList.vue'

const routes = [
  { path: '/', component: ConnectionList },
  { path: '/targets', component: TargetsList },
  { path: '/sources', component: SourcesList },
  { path: '/synchronizations', component: SynchronizationList },
  { path: '/targets/create', component: TargetTypeSelect },
  { path: '/sources/create', component: SourceTypeSelect },
  { path: '/targets/nextcloud/create', component: NextcloudTargetCreate },
  { path: '/targets/nextcloud/edit/:id', component: NextcloudTargetEdit },
  { path: '/sources/enercoop/create', component: EnercoopSourceCreate },
  { path: '/sources/enercoop/edit/:id', component: EnercoopSourceEdit },
  { path: '/connections/create', component: ConnectionCreate },
  { path: '/connections/edit/:id', component: ConnectionEdit },
  { path: '/connections', component: ConnectionList }
]

const router = createRouter({
  history: createWebHistory('/'),
  routes
})

const app = createApp(App)

app.use(PrimeVue)
app.use(ToastService)
app.use(router)

app.component('Button', Button)
app.component('InputText', InputText)
app.component('Card', Card)
app.component('Toast', Toast)
app.component('DataTable', DataTable)
app.component('Column', Column)
app.component('Dialog', Dialog)

app.mount('#app')
