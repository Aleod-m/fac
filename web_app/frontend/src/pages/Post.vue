<template>
    <div class="content">
        <h2>Post</h2>
        <label> Upload an Image </label>
        <InputFile @change="handleFileUpload($event)" accept="images/jpeg"/>
        <div v-if="show_preview" class="preview">
            <ImageView :source="img_ref" class="image"/>
            <Button @click="submitFile()" label="Submit"/>
        </div>
    </div>
</template>

<script setup lang="ts">
import { Api } from "../api";
import Button from "@/components/Button.vue";
import ImageView from "@/components/ImageView.vue";
import InputFile from "@/components/InputFile.vue";
import { ref, Ref } from "vue";

let file: File | undefined;
let show_preview: Ref<boolean> = ref(false);
let img_ref: Ref<string> = ref("");

function submitFile() {
    if (file) {
        Api.PostImage(file);
    }
}

function handleFileUpload(event: Event) {
    let target = event.target;
    if (target) {
        let files = (<HTMLInputElement>target).files;
        if (files) {
            file = files[0];
            let reader = new FileReader();
            reader.onload = function(e) {
                img_ref.value = e.target?.result?.toString()||"";
            }
            reader.readAsDataURL(file);
            show_preview.value = true;
        }
    }
}
</script>

<style scoped>
label {
    color: var(--fg);
    margin-bottom: 1em;
}

input {
    appearance: none;
    color: var(--normal);
    border: 3px solid var(--normal);
    border-radius: 1em;
    background-color: var(--dark);
    cursor: pointer;
    margin-bottom: 1em;
}
.preview {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.image {
    width: 90%;
}
</style>
