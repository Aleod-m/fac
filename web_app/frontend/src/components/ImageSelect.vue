<script setup lang="ts">
import { ref, defineProps } from "vue";
import {ImageI} from "../api_types"
const props = defineProps({
    images: {type: Array as () => Array<ImageI>, required: true },
    update: {type: Function, default: () => {}}
    // callback: {type: (id:number) => void , default: function(id:number) {return;}},
})
var selected = ref(-1);
</script>

<template>
    <div class="cont">
        <select  v-model.number="selected" @change="update(selected)" class="sel">
            <option value="-1" selected> Select Image as you want </option>
            <option v-for="image in images" :value="image.id" :key="image.id">{{image.name}}</option>
        </select>
        <div class="separator">
            <div class="arrow"></div>
        </div>
    </div>
</template>

<style scoped>
.cont {
    background-color: var(--dark);
    border: 2px solid var(--normal);
    border-radius: 1em;
    padding: .25em .25em;
    position: relative;
    width: min-content;
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    transition: 0.3s;
    /* border: solid red 1px; */
}

.cont:hover, .cont:focus {
    box-shadow: 0px 0px 5px var(--light), inset 0px 0px 2px var(--bg);
    background-color: var(--normal);

}

.arrow {
    width: 0;
    height: 0;
    border-left: 5px solid transparent;
    border-right: 5px solid transparent;
    border-top: 10px solid var(--accent-light);
    margin-right: 0.5em;
    transition: 0.3s;
}

.cont:active > .arrow {
    rotate: 180deg;
}

.separator {
    height: 100%;
    aspect-ratio: 1;
    box-sizing: border-box;
    display: flex;
    justify-content: center;
    align-items: center;
    /* background-color: var(--normal); */
}

.sel {
/* resets */
    appearance: none;
    border: none;
    -webkit-appearance: none;
    -moz-appearance: none;
    background-color: transparent;
/* border: 1px solid red; */
    line-height: 100%;
    text-align: center;
    z-index: 0;
    color: var(--light);
    text-align: center;
    padding: 0.25em 0.5em;
    padding-right: 1.5em;
}
</style>
