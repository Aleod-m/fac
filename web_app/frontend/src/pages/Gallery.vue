<script setup lang = "ts" >
import NavBar from "@/components/NavBar.vue";
import {
    computed,
    HtmlHTMLAttributes,
    onBeforeMount,
    onMounted,
    Ref,
    ref,
} from "vue";
import { Api } from "../api";
import { ImageI } from "../api_types";
import ImageView from "@/components/ImageView.vue";
import Button from "@/components/Button.vue";
import router from "@/router";

let images: Ref<Array<ImageI>> = ref([]);
Api.GetImageList(images);
let selected = ref(-1);
let images_srcs: Ref<Array<Ref<string>>> = computed(() => {
    return images.value.map((image: ImageI) => {
        let dumb = ref("");
        Api.GetImage(image.id, dumb);
        return dumb;
    });
});

function updateSelectedImage(index: number) {
    selected.value = index;
}

function deleteSelected() {
    Api.DeleteImage(images.value[selected.value].id);
    images.value.splice(selected.value, 1);
    images_srcs.value.splice(selected.value, 1);
    selected.value = -1;
}

function editImage() {
    router.push("/edit?id=" + images.value[selected.value].id);
}
function downLoadImage() {
    const link = document.createElement("a");
    link.href = images_srcs.value[selected.value].value;
    link.download = images.value[selected.value].name;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}
</script>

<template>
    <NavBar/>
    <div class="content galery-container">
        <h2>Galery</h2>
        <div class="main-image-section" v-if="selected >= 0">
            <ImageView :source="images_srcs[selected].value" class="img" />
            <div class="side-panel">
                <Button @click="editImage()" label="Edit"/>
                <Button @click="downLoadImage()" label="Download"/>
                <Button @click="deleteSelected()" label="Delete"/>
            </div>
        </div>
        <div class="image-list">
            <div
                v-for="(srcimg, index) in images_srcs"
                :key="index"
                class="card"
                @click="updateSelectedImage(index)"
            >
                <div class="imgcont">
                    <ImageView :source="srcimg.value" class="img" />
                </div>
                <label> {{ images[index].name }}</label>
            </div>
        </div>
    </div>
</template>

<style scoped>
.main-image-section {
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

.galery-container .image-list {
    width: 100%;
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    grid-gap: 1em;
    overflow: hidden;
}
.galery-container {
    padding: 1em;
}
.card {
    max-width: 600px;
    border-radius: 1em;
    padding: 0.25em;
    background-color: var(--dark);
    border: 2px solid var(--normal);
    width: auto;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    cursor: pointer;
}

.card:hover {
    border: 2px solid var(--accent-light);
}

.imgcont {
    width: 90%;
    height: 90%;
    display: flex;
    align-items: center;
    justify-content: center;
}

.img {
    border-radius: 1em;
    margin: 5px;
    width: 100%;
}
.card > label {
    width: 90%;
    font-size: 1em;
    color: var(--fg);
}
</style>
