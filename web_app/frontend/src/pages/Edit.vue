<template>
    <NavBar/>
    <div class="content">
        <h2>Edit</h2>
        <ImageView :source="img_src" class="img"/>
        <TabBar :names="algos_name" @change="SelectAlgo($event)"/>
        <div class="settings">
           <component :is="algos_components[selected]" @change="onSettingsChange($event)"/> 
        </div>
        <Button @click="SendEdit()" label="Send"/>
        <ImageView v-if="edited == true" :source="edit_image_src" />
        <div v-if="edited == true" class="side-panel">
            <Button @click="downLoadImage()" label="Download"/>
            <Button @click="PostImage()" label="Post"/>
            <!--<Button @click="Edit()" label="Edit" v-if="posted"/>-->
        </div>
    </div>
</template>

<script setup lang="ts">
// Imports
import NavBar from "@/components/NavBar.vue";
import Select from "@/components/Select.vue";
import ImageView from "@/components/ImageView.vue";
import Button from "@/components/Button.vue";
import TabBar from "@/components/TabBar.vue";
import Lum from "@/pages/EditComp/Lum.vue";
import Blur from "@/pages/EditComp/Blur.vue";
import Edge from "@/pages/EditComp/Edge.vue";
import Hue from "@/pages/EditComp/Hue.vue";
import Heq from "@/pages/EditComp/Heq.vue";
import { ref, Ref } from "vue";
import { Api, algorithms } from "@/api";
import { AlgorithmI } from "@/api_types";

// Props 
const props = defineProps<{id:number}>();

let img_src: Ref<string> = ref("");
let edit_image_src: Ref<string> = ref("");
Api.GetImage(props.id, img_src);
let edited = ref(false);
let posted = ref(false);
let url = "";

let algos = algorithms.map((algo: AlgorithmI, index: number) => {
    return { name: algo.name, id: index };
});
let algos_name = algorithms.map((algo: AlgorithmI, _: number) => algo.name);
let algos_components = [ Lum, Heq, Hue, Blur, Edge ];
let selected: Ref<number> = ref(0);
function SelectAlgo(index: number) {
    selected.value = index;
}

function onSettingsChange(val: string) {
    url = val;
}


function SendEdit() {
    Api.GetImage(props.id, edit_image_src, url, () => (edited.value = true));
}

function downLoadImage() {
    const link = document.createElement("a");
    link.href = edit_image_src.value;
    link.download = "Edited";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}

function PostImage() {
    posted.value = true;
    console.log("Todo PostImage");
}

</script>

<style scoped>
.main {
    background: var(--bg);
    padding: 20px 20px;
    border-radius: 10px;
    display: flex;
    flex-direction: column;
    flex-basis: content;
    align-items: center;
}
</style>
