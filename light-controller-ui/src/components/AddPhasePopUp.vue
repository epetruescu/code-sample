<script setup lang="ts">

import {computed, ref} from "vue";
import type {PhaseCreateRequest, PhaseDTO, SignalGroupDTO} from "@/generated";

interface PhaseCreateRequestDTO {
  greenDuration?: number;
  yellowDuration?: number;
  signalGroupIds?: Array<number>;
  sequence?: number;
}

const props = defineProps<{
  modelValue: boolean,
  title: String,
  signalGroups: SignalGroupDTO[]
  phaseToEdit: PhaseDTO | null
}>()

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
  'save': [phase: PhaseCreateRequest, isEdit: boolean, phaseId?: number]
}>()

const greenDuration = ref(10)
const yellowDuration = ref(2)
const signalGroupIds = ref([])
const sequence = ref(1)


const handleAdd = () => {
  const isEdit = !!props.phaseToEdit
  emit('save', {
    sequence: sequence.value,
    greenDuration: greenDuration.value,
    yellowDuration: yellowDuration.value,
    signalGroupIds: signalGroupIds.value
  }, isEdit, props.phaseToEdit?.id)
  emit('update:modelValue', false)
}
console.log('Add PhasePopUp component')

</script>

<template>
  <v-dialog :model-value="modelValue" @update:model-value="emit('update:modelValue', $event)"
            max-width="500">
    <v-card>
      <v-card-title>Edit Phase</v-card-title>
      <v-card-text>
        <v-text-field
          v-model="sequence"
          label="Sequence"
          type="number"
          :rules="[
            v => v > 1 || 'Sequence must be greater than 1'
          ]"/>
        <v-text-field
          v-model="greenDuration"
          label="Green Duration"
          type="number"
          :rules="[
          v => !!v || 'Green duration is required',
          v => v > 1 || 'Green duration must be greater than 1'
          ]"/>
        <v-text-field
          v-model="yellowDuration"
          label="Yellow Duration"
          type="number"
          :rules="[
            v => !!v || 'Yellow duration is required',
            v => v > 1 || 'Yellow duration must be greater than 1'
          ]"/>
        <v-combobox
          v-model="signalGroupIds"
          :items="props.signalGroups"
          item-title="name"
          item-value="id"
          label="Signal Groups"
          chips
          multiple
          :rules="[v => (v && v.length > 0) || 'At least one signal group is required']"
        ></v-combobox>

      </v-card-text>
      <v-card-actions>
        <v-spacer/>
        <v-btn @click="emit('update:modelValue', false)">Cancel</v-btn>
        <v-btn color="primary" @click="handleAdd">Save</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<style scoped>

</style>
