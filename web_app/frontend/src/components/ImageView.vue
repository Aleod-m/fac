<template>
  <img :src="source" class="img"/>
</template>

<script setup lang="ts">
import { ref, Ref, toRef } from 'vue';
const img_props = defineProps({
    source: {type: String, required: true}
});
let source= toRef(img_props, "source");
</script>

<style scoped lang="css">
.img {
    border-radius: 10px;
    border: 2px solid var(--bg);
}
</style>
