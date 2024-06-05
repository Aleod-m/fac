<script setup lang="ts">
import { ref, Ref } from "vue";
const props = defineProps<{
    name: string,
}>();
const emits = defineEmits<{
    (e:'change', val: string):void,
}>();

let inputElement: Ref<HTMLInputElement|undefined> = ref(undefined);
function onChange() {
    emits('change', inputElement.value!.value)
}
</script>

<template>
    <div class="container">
        <input ref="inputElement" id="text" class="input" placeholder="" @change="onChange"/>
        <label for="text" class="name">{{props.name}}</label>
    </div>
</template>

<style scoped>
.container {
    position:relative;
    width: 20rem;
    height: 3rem;
}

.input {
    position: absolute;
    box-sizing: border-box;
    font-size: inherit;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    border-radius: .4em;
    padding: 1.25rem;
    border: 2px solid var(--normal);
    background-color: var(--dark);
    color: var(--fg);
}

.input:hover {
    border: 2px solid var(--accent-dark);
}

.input:focus {
    border: 2px solid var(--accent-light);
}

.name {
    position: absolute;
    box-sizing: border-box;
    font-size: inherit;
    left: 1rem;
    top:0.8rem;
    padding:0 0.5rem;
    cursor: Text;
    background-color: var(--dark);
    color:var(--fg); 
    transition: top 200ms ease-in, left 200ms ease-in, font-size 200ms ease-in;
}

.input:focus ~ .name,
.input:not(:placeholder-shown).input:not(:focus) ~ .name
{
    top: -0.5rem;
    font-size: 0.8rem;
    left: 0.8rem;
}

</style>
