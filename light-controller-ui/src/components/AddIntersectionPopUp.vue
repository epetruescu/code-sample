<script setup lang="ts">
import {ref} from "vue";

const props = defineProps<{
  modelValue: boolean,
  title: string
}>()

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
  'add': [name: string]
}>()

const name = ref('')

const handleAdd = () => {
  emit('add', name.value)
  name.value = ''
  emit('update:modelValue', false)
}
</script>

<template>
  <v-dialog :model-value="modelValue" @update:model-value="emit('update:modelValue', $event)"
            max-width="500">
    <v-card>
      <v-card-title> {{ props.title }}</v-card-title>
      <v-card-text>
        <v-text-field v-model="name" label="Intersection Name"/>
      </v-card-text>
      <v-card-actions>
        <v-spacer/>
        <v-btn @click="emit('update:modelValue', false)">Cancel</v-btn>
        <v-btn color="primary" @click="handleAdd">Add</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
