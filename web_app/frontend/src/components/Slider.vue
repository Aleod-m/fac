<template>
   <div class="slider" ref="slider">
        <input type="range" :min="props.start" :max="props.end" :step="props.step">
        <span class="bar">
        </span>
        <span class="circle" @mousedown="selectKnob"></span>
        <label for=""></label>
   </div> 
</template>

<script setup lang="ts">
import { computed, getCurrentInstance, Ref, ref, toRef, watch } from "vue";
import { clerp, iclerp } from "@/utils/MathExt";

const props = defineProps<{
    start: number,
    end: number,
    step?: number,
    value?: number,
}>();
const emit = defineEmits<{
    (e:'change', val:number): void,
}>()

let pos : Ref<number> = ref(0);
let step : number = props.step || 1;
let opt_val = toRef(props, "value");
let value : Ref<number> = ref(0);
let slider: Ref<HTMLDivElement|null> = ref(null);

if (opt_val.value != undefined) {
    value.value = opt_val.value;
} else {
    value.value = props.start;
}

watch(opt_val, (new_val, _) => {
    if(new_val != undefined) {
        value.value = new_val;
        adjustKnobposFromVal(value.value);
    }
})
let t_step = computed(() => {return iclerp(props.start, props.end, step);});

function selectKnob() {
    const span = document.createElement("span");
    span.onmouseup = function() {
        document.body.removeChild(span);
    };
    span.onmousemove = adjustKnobpos;
    let body_bb = document.body.getBoundingClientRect();
    span.style.height = body_bb.height + "px";
    span.style.width = body_bb.width + "px";
    span.style.top = "0";
    span.style.position = "absolute";
    span.style.cursor = "grabbing";
    document.body.appendChild(span);
}

function adjustKnobposFromVal(val: number) {
    let t_val = iclerp(props.start, props.end, val);
    let pos_v = stepfunction(t_val);
    let slider_bb = getBoundingBox();
    pos.value = clerp(slider_bb!.height*.5, slider_bb!.width - slider_bb!.height*.5, pos_v);
    value.value = clerp(props.start, props.end, pos_v);
    emit('change', value.value);
}

function stepfunction(val:number) {
    let ret_val = val + t_step.value*.5;
    ret_val -= ret_val % t_step.value;
    return ret_val;
}

function adjustKnobpos(event: MouseEvent) {
    let mouseX = event.pageX;
    document.body.getBoundingClientRect().width
    let slider_bb = getBoundingBox();
    let t_mouseX = iclerp(slider_bb!.x + slider_bb!.height*.5, slider_bb!.x + slider_bb!.width - slider_bb!.height*.5, mouseX);
    let pos_v = stepfunction(t_mouseX)
    pos.value = clerp(0, slider_bb!.width - slider_bb!.height*.5, pos_v);
    value.value = clerp(props.start, props.end, pos_v);
    emit('change', value.value);
}

function getBoundingBox(): DOMRect | undefined {
    return slider.value!.getBoundingClientRect(); 
}
 
</script>

<style scoped lang="css">
.slider {
    position: relative;
    aspect-ratio: 10 / 1;
}

.bar {
    display: block;
    position: absolute;
    width: 100%;
    height: 50%;
    top: 25%;
    border-radius: 1em;
    background-color: var(--light);
}

.circle {
    display: block;
    position: absolute;
    height: 100%;
    left: calc(v-bind('pos') * 1px);
    aspect-ratio: 1;
    border-radius: 1em; 
    background-color: var(--normal);
    cursor: grab;
}
.circle:hover {
    background-color: var(--accent-light);
}
.circle:active {
    background-color: var(--accent-dark);
}

input {
    display: none;
    width: 100%;
    height: 100%;
}
</style>
