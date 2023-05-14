<template>
  <div class="d-flex flex-grow-1 mt-2">
    <div class="board d-flex flex-grow-1 flex-row">

      <!-- board column -->
      <div v-for="column in columns" :key="column.key" class="board-column pa-2">
        <h5>{{ $t(`board.state.${column.key}`) }}</h5>
        <div class="board-column-actions">
          <v-btn variant="plain" color="primary" @click="column.isAddVisible = !column.isAddVisible">
            <v-icon>mdi-plus</v-icon>
          </v-btn>
        </div>

        <!-- add new card form -->
        <v-card v-show="column.isAddVisible" class="pa-2 my-2 overflow-visible">
          <v-text-field
            v-model="column.addTitle"
            :label="$t('common.title')"
            :placeholder="$t('board.titlePlaceholder')"
            autofocus
            @keyup.enter="_addCard(column)"
            @keyup.esc="column.isAddVisible = false"
          ></v-text-field>
          <div class="d-flex flex-md-row flex-column">
            <v-btn class="flex-grow-1 ma-1" small @click="column.isAddVisible = !column.isAddVisible">
              {{ $t('common.cancel') }}
            </v-btn>
            <v-btn class="flex-grow-1 ma-1" small color="primary" @click="_addCard(column)">{{
                $t('common.add')
              }}
            </v-btn>
          </div>
        </v-card>
        <vue-draggable
          itemKey="name"
          :list="column.cards"
          v-bind="dragOptions"

          class="board-group"
          group="cardsGroup"
          @change="column.callback"
        >
          <template #item="{element:card}">
            <board-card
              :card="card"
              class="board-item my-2 pa-2"
              @delete="showDelete(card)"
              @edit="showEdit(card)"
            />
          </template>
        </vue-draggable>
      </div>

      <!-- edit card dialog -->
      <v-dialog v-model="editDialog" width="600">
        <v-card>
          <v-card-title class="pa-2">
            <span>{{ $t('board.editCard') }}</span>
            <v-spacer></v-spacer>
            <v-btn variant="plain" icon @click="editDialog = false">
              <v-icon>mdi-close</v-icon>
            </v-btn>
          </v-card-title>

          <v-divider></v-divider>

          <div>
            <v-text-field
              v-model="title"
              class="px-2 py-1"
              variant="plain"
              :label="$t('common.title')"
              :placeholder="$t('common.title')"
              autofocus
              hide-details
              @keyup.enter="save"
            ></v-text-field>

            <v-divider></v-divider>

            <v-textarea
              v-model="description"
              class="px-2 py-1"
              variant="plain"
              :label="$t('common.description')"
              :placeholder="$t('common.description')"
              hide-details
            ></v-textarea>
          </div>

          <v-divider></v-divider>

          <v-card-actions class="pa-2">
            <v-btn variant="outlined" @click="editDialog = false">{{ $t('common.cancel') }}</v-btn>
            <v-spacer></v-spacer>
            <v-btn color="primary" variant="elevated" @click="save">{{ $t('common.save') }}</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

    </div>
  </div>
</template>

<script lang="ts" setup>
import {ref, watch} from 'vue'
import VueDraggable from 'vuedraggable'
import BoardCard from '../components/BoardCard.vue'
import type {state, card} from '../types'
import {useI18n} from "vue-i18n";

const states = ref<Array<state>>(['TODO', 'INPROGRESS', 'TESTING', 'DONE'])


interface column {
  key: state,
  addTitle?: '',
  cards: Array<card>,
  isAddVisible: boolean,
  callback: (evt: any) => void
}

const cards = ref<Array<card>>([{
  id: 1,
  title: 'fix: 💭 channel label on chat theme',
  description: 'we need it bolder',
  order: 1,
  state: 'TODO'
}, {
  id: 2,
  title: 'feature: new emojis on board 🤘',
  description: 'we need it for reasons 🤤',
  order: 0,
  state: 'TODO'
}, {
  id: 3,
  title: 'feature: add stripe account on signup',
  description: '',
  order: 1,
  state: 'TESTING'
}, {
  id: 4,
  title: 'refactor: scroll 📜 directive on big pages',
  description: 'remember to check the scroll',
  order: 0,
  state: 'INPROGRESS'
}, {
  id: 5,
  title: 'feature: add big cards on dashboard',
  description: 'everyone loves cards',
  order: 3,
  state: 'TODO'
}])
const columns = ref<Array<column>>([])
const editDialog = ref(false)
const cardToEdit = ref<card>()
const title = ref<string | undefined>('')
const description = ref('')
const cardToDelete = ref<card>()

const dragOptions = reactive({
  animation: 200,
  group: 'description',
  disabled: false,
  ghostClass: 'ghost'
})
const parse = (cards: Array<card>) => {
  if (!cards) return columns.value.map((col) => (col.cards = []))
  return columns.value.forEach((col) => {
    col.cards = cards
      .filter((card) => card.state === col.key)
      .sort((a, b) => a.order < b.order ? -1 : 0)
  })
}
const _updateOrder = () => {
  let tmp: Array<card> = []
  columns.value.forEach((col) => {
    tmp = [
      ...tmp,
      ...col.cards
    ]
  })
  cards.value = tmp
}
const changeState = (evt: any, colIndex: number) => {

  if (evt.added || evt.moved) {
    const column = columns.value[colIndex]
    const state = column.key

    for (let i = 0; i < column.cards.length; i++) {
      column.cards[i].order = i
      column.cards[i].state = state
    }
    _updateOrder()
  }
}

watch(cards, (n) => {
  parse(n)
}, {deep: true})

onMounted(() => {
  parse(cards.value)
})

onBeforeMount(() => {
  states.value.forEach((state, index) => {
    columns.value.push({
      key: state,
      cards: [],
      isAddVisible: false,
      callback: (evt) => changeState(evt, index)
    })
  })
})

const showEdit = (card: card) => {
  cardToEdit.value = card
  title.value = card.title
  description.value = card.description
  editDialog.value = true
}
const updateCard = (card: card) => {
  const cardIdx = cards.value.find((t) => t.id === card.id)
  if (cardIdx)
    Object.assign(cardIdx, card)
}
const showDelete = (card: card) => {
  cardToDelete.value = card
  showDeleteDialog(cardToDelete.value)
}
const deleteCard = (card: card) => {
  const cardIdx = cards.value.findIndex((t) => t.id === card.id)

  if (cardIdx !== -1) cards.value.splice(cardIdx, 1)
}
const save = () => {
  if (cardToEdit.value) {
    updateCard({
      ...cardToEdit.value,
      title: title.value,
      description: description.value
    })
  }
  editDialog.value = false
}
const addCard = (card: card) => {
  cards.value.push({
    ...card,
    id: '_' + Math.random().toString(36).substr(2, 9),
  })
}
const _addCard = async (column: column) => {

  const {addTitle, key} = column

  addCard({
    state: key,
    title: addTitle,
    description: '',
    order: -1,
    id: -1
  })

  for (let i = 0; i < column.cards.length; i++) {
    column.cards[i].order = i
  }
  await nextTick()
  _updateOrder()
  column.addTitle = ''
}


const {t} = useI18n()
const showDeleteDialog = (card: card) => {
  const dialog = window.$dialog?.show({
    title: t('common.delete'),
    main: t('board.deleteDescription'),
    confirmText: t('common.delete'),
    confirm: () => {
      dialog?.confirmLoading(true)
      setTimeout(() => {
        dialog?.confirmLoading(false)
        window.$snackBar?.info(`delete card '${card.title}' success`)
        deleteCard(card)
        dialog?.close()
      }, 1000)
    }
  })
}

</script>

<style lang="scss" scoped>
.ghost {
  opacity: 0.5;
  background: var(--v-primary-lighten1) !important;
}

.board {
  display: flex;
  overflow-x: scroll;

  .board-column {
    position: relative;
    display: flex;
    flex: 1;
    flex-direction: column;
    min-width: 180px;

    &-actions {
      position: absolute;
      top: 12px;
      right: 6px;
    }
  }

  .board-group {
    min-height: 100%;
  }

  .board-item {
    position: relative;
    min-height: 54px;
    transition: transform 0.2s;
    cursor: pointer;

    > a {
      display: block;
      padding: 8px;
    }

    &:hover {
      transform: translateY(-6px);
    }

    &.sortable-chosen {
      cursor: all-scroll;
    }
  }
}

.v-application--is-rtl {
  .board-column-actions {
    left: 6px;
    right: auto;
  }
}
</style>
