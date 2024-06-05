<script setup lang="ts">
import { ref, Ref, toRef, watch } from "vue";
const props = defineProps<{
    text: string,
    placeholder?: string 
}>();
const emits = defineEmits<{
    (e:'change', val:string): void,
}>();

let input_tag: Ref<HTMLInputElement|null> = ref(null);
let text: Ref<string> = ref(props.text);

watch(toRef(props, "text"), (new_val, _) => {
    text.value = new_val;
});

function onChange() {
    text.value = input_tag.value!.value;
    emits('change', text.value);
}

</script>

<template>
    <div>
        <input ref="input_tag" class="input" :placeholder="props.placeholder" :value="text" @change="onChange"/>
    </div>
</template>

<style scoped>
.input {
    box-sizing: border-box;
    font-size: inherit;
    width: 100%;
    height: 100%;
    border-radius: .4em;
    padding: 0.5em 1.25rem;
    border: 2px solid var(--normal);
    background-color: var(--dark);
    color: var(--fg);
}

.input:hover {
    border: 2px solid var(--accent-light);
}

.input:focus {
    border: 2px solid var(--accent-dark);
}
</style>
