<script setup lang="ts">
import NavBar from "@/components/NavBar.vue";
import {
    computed,
    onBeforeMount,
    onMounted,
    ref,
    Ref,
    toRef,
    watch,
} from "vue";
import Select from "@/components/Select.vue";
import ImageView from "@/components/ImageView.vue";
import Button from "@/components/Button.vue";
import { Api } from "@/api";
import { ImageI } from "@/api_types";
import { SelectI } from "@/app_types";
import router from "@/router";

let images: Ref<Array<ImageI>> = ref([]);
Api.GetImageList(images);
let img_select: Ref<Array<SelectI<number>>> = computed(() =>
    images.value.map((image: ImageI, index: number) => {
        return { name: image.name, id: index };
    })
);

// creating the call back for the image view on the image selec change;
let def: Ref<string> = ref("Select an image");
let img_src: Ref<string> = ref("");
let selected: Ref<number> = ref(-1);

function ChangeImageId(index: number) {
    Api.GetImage(images.value[index].id, img_src);
    selected.value = index;
}

function editImage() {
    router.push("/edit/" + images.value[selected.value].id).then();
}
function downLoadImage() {
    const link = document.createElement("a");
    link.href = img_src.value;
    link.download = images.value[selected.value].name;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}
function deleteSelected() {
    Api.DeleteImage(images.value[selected.value].id);
    images.value.splice(selected.value, 1);
    selected.value = -1;
}
</script>

<template>
    <NavBar/>
    <div class="content">
        <h2>Select</h2>
        <Select
            :options="img_select"
            :default="def"
            :callBack="ChangeImageId"
        />
        <div class="main-image-section" v-if="selected != -1">
            <ImageView :source="img_src" class="image" />
            <div class="side-panel">
                <Button @click="editImage()" label="Edit"/>
                <Button @click="downLoadImage()" label="Download"/>
                <Button @click="deleteSelected()" label="Delete"/>
            </div>
        </div>
    </div>
</template>

<style scoped>


.img-select {
    margin: 1em;
}

.image {
    width: 90%;
}

.main-image-section {
    width: 90%;
    margin-bottom: 1em;
    display: flex;
    flex-direction: row;
}
.side-panel {
    margin-left: 1em;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
}

</style>
