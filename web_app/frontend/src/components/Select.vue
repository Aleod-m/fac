<template>
    <div class="select" id="sel" @click="clicked">
        <p id="select_text">{{ placeholder }}</p>
        <div class="dropdown" id="dp" v-show="open">
            <div class="scroll">
                <div
                    @click="optClicked(option)"
                    class="opt"
                    v-for="option in options"
                    :key="option.id"
                >
                    {{ option.name }}
                </div>
            </div>
        </div>
        <a class="arrow-container">
            <div class="downarrow" id="down"></div>
        </a>
    </div>
</template>

<script setup lang="ts">
import { ref } from "@vue/reactivity";
import { Ref } from "vue";
import { SelectI } from "../app_types";
const props = defineProps({
    default: { type: String, default: "select an option" },
    options: { type: Array as () => Array<SelectI<any>>, required: true },
    callBack: { type: Function, default: () => {} },
});
let open: Ref<boolean> = ref(false);
let placeholder = ref(props.default);

function clicked(): void {
    let downarrow = document.getElementById("down");
    if (open.value) {
        downarrow?.classList.remove("return-arrow");
        open.value = false;
    } else {
        downarrow?.classList.add("return-arrow");
        open.value = true;
    }
}

function optClicked(option: SelectI<any>): void {
    let downarrow = document.getElementById("down");
    downarrow?.classList.remove("return-arrow");
    props.callBack(option.id);
    let text = document.getElementById("select_text");
    if (text) {
        text.textContent = option.name.toString();
    }
}
</script>

<style scoped>
.select {
    background-color: var(--dark);
    border: 2px solid var(--normal);
    box-sizing: border-box;
    min-width: max-content;
    border-radius: 0.4em;
    position: relative;
    padding: 0.25em 0.25em;
    padding-right: calc(0.25em + 32px);
    margin: 0;
    display: flex;
    margin: 10px;
    clip: auto;
    cursor: pointer;
}

.select > :first-child {
    color: var(--fg);
    background: none;
    border: none;
    padding: 0;
    margin: 0 0.5em;
    margin-top: 0.1em;
}

.arrow-container {
    border: none;
    padding: 0;
    margin: 0;
    position: absolute;
    display: flex;
    align-items: center;
    justify-content: center;
    top: 0;
    right: 0;
    height: 100%;
    aspect-ratio: 1;
    background-color: transparent;
    border-radius: 1em;
}

.downarrow {
    width: 0;
    height: 0;
    border-left: 5px solid transparent;
    border-right: 5px solid transparent;
    border-top: 10px solid var(--accent-light);
    transition: 0.3s;
}

.return-arrow {
    transform: rotate(180deg);
}

.dropdown {
    position: absolute;
    display: flex;
    flex-direction: column;
    left: 0;
    border: 2px solid var(--normal);
    border-radius: 0.4em;
    transform: translate(0, 2em);
    width: min-content;
    overflow: hidden;
    overflow-y: scroll;
    max-height: 10em;
    scrollbar-width: none;
}

.opt {
    border: none;
    box-sizing: border-box;
    display: inline-block;
    width: 100%;
    border-left: 2px solid var(--dark);
    background-color: var(--dark);
    color: var(--fg);
    padding: 0.5em 1em;
}

.select:hover {
    border-color: var(--accent-light);
    background-color: var(--normal);
}

.select:active {
    border-color: var(--accent-dark);
}

.opt:hover {
    border-left-color: var(--accent-light);
    background-color: var(--normal);
}
.opt:active {
    border-left-color: var(--accent-dark);
}
</style>
