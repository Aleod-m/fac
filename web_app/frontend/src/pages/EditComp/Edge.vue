<template>
    <p>Edge detection algorithm.</p>
</template>

<script setup lang="ts">
import { algorithms } from "@/api";
import { AlgorithmI } from "@/api_types";

const emits = defineEmits<{
    (e:'change', val: string):void,
}>();
const algo = algorithms.filter((algo: AlgorithmI, _:number) => algo.name == "Edge");
emits('change', algo[0].url({}));
</script>

<style scoped>

</style>
