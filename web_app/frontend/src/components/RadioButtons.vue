<script setup lang="ts">
const props = defineProps({
    options : {
        type: Array as () => Array<string>,
        required: true,
    },
    
});
const emits = defineEmits<{
    (e:'change', val:number):void,
}>();
</script>

<template>
    <div>
        <label
            class="radio-button"
            v-for="option,index in props.options"
            :key="index"
            @click="emits('change', index)"
        >
            <input type="radio" name="radio">
                <span class="label-visible">
                    <span class="fake-radiobutton">
                        <span class="fake-disk"></span>
                    </span>
                    {{option}}
              </span>
        </label>
    </div>
</template>

<style scoped>
.radio-button {
    position: relative;
    cursor: pointer;
    font-size: 1em;
    color: var(--fg);
}

.radio-button input {
    position: absolute;
    border: 1px solid green;
    opacity: 0;
}
.radio-button .label-visible {
    margin-left: 2em;
    margin-bottom: 0;
}

.fake-radiobutton {
    position: absolute;
    display: block;
    top: .15em;
    left: .5em;
    width: 1em;
    height: 1em;
    border: 2px solid var(--normal);
    border-radius: 50%; 
    background-color: var(--dark);
}
 
.fake-disk{
    display: none;
    position: absolute;
    top: 50%;
    left: 50%;
    width: .8em;
    height: .8em;
    border-radius: 50%;
    background: var(--accent-light);
    transform: translateX(-50%) translateY(-50%);
}

input[type="radio"]:checked + span .fake-radiobutton > .fake-disk {
    display: block;
}
</style>
