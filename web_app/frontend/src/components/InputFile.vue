<template>
    <form action="#">
        <div class="input-file-container" >  
            <input class="input-file" 
               type="file"
               id="file-selector"
               placeholder=""
               @change="fileChange($event)"
               accept="image/jpeg"/>
            <label tabindex="0" for="file-selector" class="input-file-trigger">{{text}}</label>
        </div>
        <p class="file-return"></p>
    </form>
</template>

<script setup lang="ts">
import { ref, Ref } from "vue";
const props = defineProps({
    accept: String,
});
const emits = defineEmits<{
    (e:'change', event: Event):void
}>();

let text: Ref<string> = ref("Select a file ...");
function fileChange(event: Event) {
    emits('change', event)
    let target = event.target;
    if(target) {
        let files = (<HTMLInputElement>target).files;
        if (files)
            text.value = files[0].name;
    }
}
</script>

<style scoped>
.input-file-container {
    position: relative;
    width: max-content;
    height: max-content;
} 
.input-file {
    position: absolute;
    top: 0; left: 0;
    width: 100%;
    opacity: 0;
    cursor: pointer;
}
.input-file-trigger {
    display: block;
    padding: 0.5em 1em;
    border-radius: .4em;
    background: var(--dark);
    border: 2px solid var(--normal);
    color: var(--fg);
    transition: all .4s;
    cursor: pointer;
}

.input-file:focus + .input-file-trigger,
.input-file-trigger:focus {
    border-color: var(--accent-dark);
}

.input-file:hover + .input-file-trigger,
.input-file-trigger:hover {
    border-color: var(--accent-light);
}
</style>
