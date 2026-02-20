<script setup lang="ts">
import {reactive, ref} from 'vue'
import AddIntersectionDialog from './components/AddIntersectionPopUp.vue'
import axios from 'axios'
import {onMounted} from 'vue'
import type {IntersectionDTO, PhaseDTO, SignalGroupDTO} from './generated'

import {useApi} from './useApis'
import IntersectionDetails from "@/components/IntersectionDetails.vue";

const {intersectionApi, phaseApi} = useApi()
const intersections = ref<IntersectionDTO[]>([])
const selectedIntersection = ref<IntersectionDTO | null>(null)
const phases = ref<PhaseDTO[]>([])
const signalGroups = ref<SignalGroupDTO[]>([])
const showAddIntersection = ref(false)
const newIntersectionName = ref('')
const store = reactive({
  count: 0
})
const props = defineProps<{
  selectedIntersection: IntersectionDTO | null
}>()

const showDeleteConfirm = ref(false)
const intersectionToDelete = ref<IntersectionDTO | null>(null)

const activeLight = ref<string>('red')

const fetchIntersections = async () => {
  try {
    const intersectionResponse = await intersectionApi.getIntersectionList()
    intersections.value = intersectionResponse.data
    console.log("Fetching intersections")
  } catch (error) {
    console.error('Failed to fetch intersections:', error)
  }
}

const addIntersection = async (name: string) => {
  console.log("Add was triggered " + name)
  if (!name.trim()) return
  try {
    await intersectionApi.createIntersection({name})
    showAddIntersection.value = false
    console.log("Adding intersection")
    await fetchIntersections()
  } catch (error) {
    console.error('Failed to add intersection:', error)
  }
}

const activeIntersection = async (intersection: IntersectionDTO) => {
  try {
    await intersectionApi.updateIntersection(intersection.id!, {
      active: !intersection.active
    })
    await fetchIntersections()
    if (selectedIntersection.value?.id === intersection.id && selectedIntersection.value !== null) {
      selectedIntersection.value.active = !intersection.active
    }
  } catch (error) {
    console.error('Failed to toggle intersection:', error)
  }
}
const deleteIntersection = async () => {
  if (!intersectionToDelete.value) return
  try {
    await intersectionApi.deleteIntersection(intersectionToDelete.value.id!)
    if (selectedIntersection.value?.id === intersectionToDelete.value.id) {
      selectedIntersection.value = null
    }
    await fetchIntersections()
    showDeleteConfirm.value = false
    intersectionToDelete.value = null
  } catch (error) {
    console.error('Failed to delete intersection:', error)
  }
}

const confirmDelete = (intersection: IntersectionDTO) => {
  intersectionToDelete.value = intersection
  showDeleteConfirm.value = true
}

const handleActiveLightChange = () => {
  axios.post('http://localhost:8080/intersections', {activeLight: activeLight.value})
    .then(console.log)
    .catch(console.error)
}
onMounted(() => {
  fetchIntersections()
})
</script>

<template>
  <v-app>
    <v-navigation-drawer permanent :width="210">
      <v-list-item title="Light Controller" subtitle="Eds App"></v-list-item>
      <v-divider></v-divider>
      <v-list-item link title="Add Intersection" @click="showAddIntersection = true"></v-list-item>
      <v-list-item v-for="intersection in intersections" :key="intersection.id" link
                   :title="intersection.name"
                   @click="selectedIntersection = intersection">
        <template v-slot:append>
          <v-btn
            variant="text"
            icon
            size="x-small"
            @click.stop="activeIntersection(intersection)"
          >
            <v-icon :color="intersection.active ? 'success' : 'error'">
              {{ intersection.active ? 'mdi-power-settings-outline' : 'mdi-power-off' }}
            </v-icon>
          </v-btn>
          <v-btn icon size="small" variant="text" @click.stop="confirmDelete(intersection)">
            <v-icon color="red">mdi-trash-can-outline</v-icon>
          </v-btn>
        </template>
      </v-list-item>
    </v-navigation-drawer>


    <v-app-bar :title="selectedIntersection?.name || 'No Intersection Selected'">

    </v-app-bar>
    <v-main>
      <IntersectionDetails :selected-intersection="selectedIntersection"/>
      <!-- main content -->
    </v-main>
    <AddIntersectionDialog v-model="showAddIntersection" title="Add Intersection"
                           @add="addIntersection"/>
    <v-dialog v-model="showDeleteConfirm" max-width="400">
      <v-card>
        <v-card-title>Confirm Delete</v-card-title>
        <v-card-text>
          Are you sure you want to delete "{{ intersectionToDelete?.name }}"?
        </v-card-text>
        <v-card-actions>
          <v-spacer/>
          <v-btn @click="showDeleteConfirm = false">Cancel</v-btn>
          <v-btn color="error" @click="deleteIntersection">Delete</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

  </v-app>
  <!--
    <div class="wrapper">

      <div class="title_container">
          Title
      </div>
      <div class="content_container">

&lt;!&ndash;        <div class="left_container">
          &lt;!&ndash;      list of obects&ndash;&gt;


          left pane
        </div>&ndash;&gt;
        <div class="main_container">
          &lt;!&ndash;      contents of the object selected&ndash;&gt;
          main content
        </div>
      </div>
    </div>-->
  <!--    <div class="light-controller">-->
  <!--      <div class="light">-->
  <!--        <label>-->
  <!--          <input type="radio" value="red" class="red" v-model="activeLight" name="light"-->
  <!--            @change="handleActiveLightChange" /> Red-->
  <!--        </label>-->
  <!--        <label>-->
  <!--          <input type="radio" value="yellow" class="yellow" v-model="activeLight" name="light"-->
  <!--            @change="handleActiveLightChange" /> Yellow-->
  <!--        </label>-->
  <!--        <label>-->
  <!--          <input type="radio" value="green" class="green" v-model="activeLight" name="light"-->
  <!--            @change="handleActiveLightChange" /> Green-->
  <!--        </label>-->
  <!--      </div>-->

  <!--      <p>Active light: {{ activeLight }}</p>-->
  <!--    </div>-->
  <!--  </main>-->
</template>

<style scoped>
header {
  line-height: 1.5;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    margin: calc(var(--section-gap) / 4);
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }
}

.light-controller {
  display: grid;
  place-items: center;
  gap: 1rem;

  .light {
    display: grid;
    gap: .5rem;
  }

}

input[type='radio'].red {
  accent-color: #cc3232;
}

input[type='radio'].yellow {
  accent-color: #e7b416;
}

input[type='radio'].green {
  accent-color: #2dc937;
}
</style>
