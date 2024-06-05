<script setup lang="ts">
const props = defineProps({
    label: String,
});
const emits = defineEmits<{
    (e:'click'):void
}>();
</script>

<template>
    <button @click="emits('click')">{{props.label}}</button>
</template>

<style scoped>
button {
    margin: 1em;
    border-radius: 1em;
    font-size:1em;
    border: 2px solid var(--normal);
    background-color: var(--dark);
    color: var(--fg);
    padding: 0.25em 0.5em;
    cursor: pointer;
}

button:hover {
    border-color: var(--accent-light);
    background-color: var(--normal);
}

button:active {
    border-color: var(--accent-dark);
}
</style>
