<script setup lang="ts">

import {computed, onMounted, ref, watch} from "vue";
import {useApi} from '../useApis'
import type {IntersectionDTO, PhaseCreateRequest, PhaseDTO, SignalGroupDTO} from "@/generated";
import {useDisplay} from "vuetify/framework";
import AddIntersectionDialog from "@/components/AddIntersectionPopUp.vue";
import AddSignalGroupPopUp from "@/components/AddSignalGroupPopUp.vue";
import AddPhasePopUp from "@/components/AddPhasePopUp.vue";

const {intersectionApi, phaseApi, signalGroupApi} = useApi()

const showAddSignalGroupPopUp = ref(false)
const showAddPhasePopUp = ref(false)

const props = defineProps<{
  selectedIntersection: IntersectionDTO | null
}>()
const intersection = ref<IntersectionDTO>()

const phaseToEdit = ref<PhaseDTO | null>(null)

watch(() => props.selectedIntersection, (newValue) => {
  if (newValue) {
    fetchIntersectionDetails()
  }
}, {immediate: true})
const editPhase = (phase: PhaseDTO) => {
  phaseToEdit.value = phase
  showAddPhasePopUp.value = true
}

const highLightSignalGroups = (phase: PhaseDTO | null) => {

}

interface PhasePopupCreateRequest {
  greenDuration?: number;
  yellowDuration?: number;
  sequence?: number;
  signalGroupIds?: Array<number>;
}

const phaseShowDeleteConfirm = ref(false)
const phaseToDelete = ref<PhaseDTO | null>(null)
const selectedPhase = ref<PhaseDTO | null>(null)
const {lg, sm} = useDisplay()

const cols = computed(() => {
  return lg.value ? [3, 9]
    : sm.value ? [9, 3]
      : [6, 6]
})

const fetchIntersectionDetails = async () => {
  try {
    if (props.selectedIntersection?.id !== undefined) {
      const intersectionResponse = await intersectionApi.getIntersection(props.selectedIntersection.id)
      intersection.value = intersectionResponse.data
      console.log("Fetching intersections on mount")
    }
  } catch (error) {
    console.error('Failed to fetch intersections:', error)
  }
}

const addSignalGroup = async (signalGroupName: string) => {
  try {
    if (props.selectedIntersection?.id !== undefined) {
      const intersectionResponse = await signalGroupApi.createSignalGroup({
        intersectionId: props.selectedIntersection.id,
        name: signalGroupName
      })
      intersection.value = intersectionResponse.data
      console.log("Fetching intersections")
      await fetchIntersectionDetails()

    }
  } catch (error) {
    console.error('Failed to fetch intersections:', error)
  }
}

const savePhase = async (phaseData: PhaseCreateRequest, isEdit: boolean, phaseId?: number) => {
  try {
    if (props.selectedIntersection?.id !== undefined) {
      const selectedSignalGroupIds = phaseData?.signalGroupIds?.map(item => 
        typeof item === 'number' ? item : (item as any).id
      ).filter((id): id is number => id !== undefined) || []
      
      console.log("Selected ids", selectedSignalGroupIds)
      console.log("Phase data being sent:", {
        greenDuration: Number(phaseData.greenDuration),
        signalGroupIds: selectedSignalGroupIds,
        yellowDuration: Number(phaseData.yellowDuration),
        sequence: Number(phaseData.sequence)
      })
      
      if (isEdit && phaseId) {
        await phaseApi.updatePhase(phaseId, {
          greenDuration: Number(phaseData.greenDuration),
          signalGroupIds: selectedSignalGroupIds,
          yellowDuration: Number(phaseData.yellowDuration),
          sequence: Number(phaseData.sequence)
        })
      } else {
        await phaseApi.createPhase({
          intersectionId: props.selectedIntersection.id,
          greenDuration: Number(phaseData.greenDuration),
          signalGroupIds: selectedSignalGroupIds,
          yellowDuration: Number(phaseData.yellowDuration),
          sequence: Number(phaseData.sequence)
        })
      }
      
      await fetchIntersectionDetails()
      phaseToEdit.value = null
      console.log("Phase saved successfully")
    }
  } catch (error) {
    console.error('Failed to save phase:', error)
  }
}

const deletePhase = async () => {
  if (!phaseToDelete.value) return
  try {
    await phaseApi.deletePhase(phaseToDelete.value.id!)
    if (selectedPhase.value?.id === phaseToDelete.value.id) {
      selectedPhase.value = null
    }
    phaseShowDeleteConfirm.value = false
    phaseToDelete.value = null
  } catch (error) {
    console.error('Failed to delete intersection:', error)
  }
}

const phaseConfirmDelete = (phase: PhaseDTO) => {
  phaseToDelete.value = phase
  phaseShowDeleteConfirm.value = true
}

onMounted(() => {
  fetchIntersectionDetails()
})
</script>

<template>


  <v-container>
    <v-row
      class="mb-6"
      no-gutters
    >
      <v-col :cols="cols[0]">
        <v-btn :disabled="!selectedIntersection || selectedIntersection.active"
               @click="phaseToEdit = null; showAddPhasePopUp = true">
          Add Phase
        </v-btn>
        <v-sheet class="pa-2 ma-2">
        </v-sheet>
        <v-sheet v-for="phase in intersection ?.phases"
                 :key="phase.id"
                 class="pa-2 ma-2"
                 link
                 :title="`Phase ${phase.sequence}`"
                 @click="highLightSignalGroups(phase)">
          <div class="d-flex justify-space-between align-center">
            <span>Phase {{ phase.sequence }}</span>
            <div>
              <v-btn
                icon
                size="small"
                variant="text"
                @click.stop="editPhase(phase)"
              >
                <v-icon color="orange">mdi-pencil-outline</v-icon>
              </v-btn>
              <v-btn
                icon
                size="small"
                variant="text"
                @click.stop="phaseConfirmDelete(phase)"
              >
                <v-icon color="red">mdi-trash-can-outline</v-icon>
              </v-btn>
            </div>
          </div>
        </v-sheet>
      </v-col>

      <v-col :cols="cols[1]">
        <v-btn :disabled="!selectedIntersection || selectedIntersection.active"
               @click="showAddSignalGroupPopUp = !showAddSignalGroupPopUp">
          Add Signal Group
        </v-btn>
        <v-sheet
          v-for="signalGroup in intersection ?.signalGroups"
          :key="signalGroup.id"
          class="pa-2 ma-2"
        >
          <div class="d-flex justify-space-between align-center">
            <span>{{ signalGroup.name }}</span>
            <v-icon :color="signalGroup?.currentColor?.toLowerCase() || 'red'">
              mdi-circle
            </v-icon>
          </div>
        </v-sheet>
      </v-col>
    </v-row>
  </v-container>

  <AddPhasePopUp
    v-model="showAddPhasePopUp"
    title="Add Phase"
    :signal-groups="intersection?.signalGroups || []"
    :phase-to-edit="phaseToEdit"
    @save="savePhase"
  />
  <AddSignalGroupPopUp v-model="showAddSignalGroupPopUp" @add="addSignalGroup"/>

  <v-dialog v-model="phaseShowDeleteConfirm" max-width="400">
    <v-card>
      <v-card-title>Confirm Delete</v-card-title>
      <v-card-text>
        Are you sure you want to delete "{{ phaseToDelete?.sequence }}"?
      </v-card-text>
      <v-card-actions>
        <v-spacer/>
        <v-btn @click="phaseShowDeleteConfirm = false">Cancel</v-btn>
        <v-btn color="error" @click="deletePhase">Delete</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

</template>

<style scoped>

</style>
