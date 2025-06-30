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
import Menu from 'primevue/menu'

import 'primevue/resources/themes/lara-light-blue/theme.css'
import 'primevue/resources/primevue.min.css'
import 'primeicons/primeicons.css'
import 'primeflex/primeflex.css'
import './style.css'

import App from './App.vue'
import NextcloudTargetCreate from './views/targets/nextcloud/NextcloudTargetCreate.vue'
import NextcloudTargetEdit from './views/targets/nextcloud/NextcloudTargetEdit.vue'
import EnercoopSourceCreate from './views/sources/enercoop/EnercoopSourceCreate.vue'
import EnercoopSourceEdit from './views/sources/enercoop/EnercoopSourceEdit.vue'
import FreeSourceCreate from './views/sources/free/FreeSourceCreate.vue'
import FreeSourceEdit from './views/sources/free/FreeSourceEdit.vue'
import IleoSourceCreate from './views/sources/ileo/IleoSourceCreate.vue'
import IleoSourceEdit from './views/sources/ileo/IleoSourceEdit.vue'
import ConnectionCreate from './views/connections/ConnectionCreate.vue'
import ConnectionEdit from './views/connections/ConnectionEdit.vue'
import ConnectionList from './views/connections/ConnectionList.vue'
import SourcesList from './views/sources/SourcesList.vue'
import TargetsList from './views/targets/TargetsList.vue'
import SourceTypeSelect from './views/sources/SourceTypeSelect.vue'
import TargetTypeSelect from './views/targets/TargetTypeSelect.vue'
import SynchronizationList from './views/synchronizations/SynchronizationList.vue'

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
  { path: '/sources/free/create', component: FreeSourceCreate },
  { path: '/sources/free/edit/:id', component: FreeSourceEdit },
  { path: '/sources/ileo/create', component: IleoSourceCreate },
  { path: '/sources/ileo/edit/:id', component: IleoSourceEdit },
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
app.component('Menu', Menu)

app.mount('#app')
