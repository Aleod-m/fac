<template>
    <p>Equalize either the staturation or the value of the image.</p>
    <div class="container">
        <RadioButtons class=".buttons" :options="options" @change="onChange($event)"/>
    </div>
</template>

<script setup lang="ts">
import { Ref, ref } from "vue";
import RadioButtons from "@/components/RadioButtons.vue";
import { algorithms } from "@/api"
import { AlgorithmI } from "@/api_types"

const emits = defineEmits<{
    (e:'change', val: string):void,
}>();

const options = ["saturation", "value"];
const options_val = ["s", "v"];
const algo = algorithms.filter((algo: AlgorithmI, _:number) => algo.name == "Heq")

let selected: Ref<number> = ref(0);

function onChange(val: number) {
    selected.value = val;
    emits('change', algo[0].url(options_val[val]));
}
</script>

<style scoped>
.container {
    display: flex;
    flex-direction: row;
    width: 20em;
    justify-content: space-around;
    align-items: center;
}
.buttons {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}
</style>
