<template>
    <p>Change the global color of the image.</p>
    <div class="container">
        <Slider class="slider" :value="lum_val" :start="0" :end="360" :step="1" @change="slider"/>
        <InputField class="input_field" :text="text" @change="inputfield"/> 
    </div>
</template>

<script setup lang="ts">
import {computed, Ref, ref, watch} from "vue";
import Slider from "@/components/Slider.vue";
import InputField from "@/components/InputField.vue";
import { algorithms } from "@/api"
import { AlgorithmI } from "@/api_types"
const emits = defineEmits<{
    (e:'change', url: string):void,
}>();

let lum_val: Ref<number> = ref(0);
let text: Ref<string> = ref("0");
const algo = algorithms.filter((algo: AlgorithmI, _:number) => algo.name == "Hue")

watch(lum_val, (new_val, _) => {
    text.value = lum_val.value.toString();
})

function slider(val:number) {
    lum_val.value = val;
    emits('change', algo[0].url(Math.round(val)));
}

function inputfield(txt: string) {
    let parse: number = parseInt(txt);
    if (!isNaN(parse)) {
        lum_val.value = parse;
        emits('change', algo[0].url(parse));
    }
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
.slider {
    height: 20px;
}
.input_field {
    width: 5em;
}
</style>
