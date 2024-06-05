import { createApp } from 'vue'
import App from './App.vue'
import Vue from 'vue'
import Home from './pages/Home.vue'
import Post from './pages/Post.vue'
import Galery from './pages/Galery.vue'
import Edit from './pages/Edit.vue'
import Select from './pages/Select.vue'
import router from './router';




createApp(App).use(router).mount('#app')

