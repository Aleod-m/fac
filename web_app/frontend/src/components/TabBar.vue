<script setup lang="ts">
import { Ref, ref } from "vue";
const props = defineProps({
    names : {
        type: Array as () => Array<String>,
        required: true,
    },
});
const emits = defineEmits<{
    (e:'change', ind: number):void,
}>();

let selected: Ref<number> = ref(0);
function onSelect(id: number) {
    selected.value = id;
    emits('change', id);
    for(let i = 0; i < props.names.length; i++) {
        document.getElementById(i.toString())?.classList?.remove("selected");
    }
    document.getElementById(id.toString())?.classList?.add("selected");
}
onSelect(0)
</script>

<template>
   <div class="tabbar">
        <div class="tab"
             v-for="name, index in props.names"
             @click="onSelect(index)"
             :id="index.toString()"
             :key="index">
            {{name}}
        </div>
   </div>
</template>

<style scoped>
.tabbar {
    width: 100%;
    height:min-content;
    display: flex;
    flex-direction: row;
    justify-content: space-evenly;
    align-items: center;
}

.tab:first-child {
    border-left-width: .2em;
}
.tab:last-child {
    border-right-width: .2em;
}

.tab {
    font-size: 1em;
    background-color: var(--bg);
    box-sizing: border-box;
    color: var(--fg);
    height: 100%;
    border: 2px solid var(--normal);
    border-width: .2em .1em .2em .1em;
    border-radius: .2em .2em 0 0;
    flex-basis: calc(100% / v-bind('props.names.length'));
    cursor: pointer;
    display: flex;
    box-sizing: border-box;
    text-decoration: none;
    align-items: center;
    justify-content: center;
    transition: 0.3s;
}
.tab:hover {
    background-color: var(--normal);
}

.selected {
    background-color: var(--dark);
    border-bottom-color: var(--dark);
}

</style>
