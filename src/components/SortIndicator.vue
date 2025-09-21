<template>
  <svg
    xmlns="http://www.w3.org/2000/svg"
    :class="[
      'h-3 w-3 transition-all duration-200',
      isActive ? 'text-primary opacity-100' : 'text-base-content/40 opacity-60 group-hover:text-base-content/60'
    ]"
    fill="none"
    viewBox="0 0 24 24"
    stroke="currentColor"
  >
    <!-- Ascending (up arrow) -->
    <path
      v-if="props.sortField === props.field && props.sortOrder === 'asc'"
      stroke-linecap="round"
      stroke-linejoin="round"
      stroke-width="2"
      d="M5 15l7-7 7 7"
    />
    <!-- Descending (down arrow) -->
    <path
      v-else-if="props.sortField === props.field && props.sortOrder === 'desc'"
      stroke-linecap="round"
      stroke-linejoin="round"
      stroke-width="2"
      d="M19 9l-7 7-7-7"
    />
    <!-- Default (up-down arrows) -->
    <g v-else stroke-width="2">
      <path
        stroke-linecap="round"
        stroke-linejoin="round"
        d="M7 16V4m0 0L3 8m4-4 4 4m6 0v12m0 0 4-4m-4 4-4-4"
      />
    </g>
  </svg>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  field: {
    type: String,
    required: true
  },
  sortField: {
    type: String,
    default: ''
  },
  sortOrder: {
    type: String,
    default: 'desc',
    validator: (value) => ['asc', 'desc'].includes(value)
  }
})

const isActive = computed(() => props.sortField === props.field)
</script>
