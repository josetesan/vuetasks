<template>
  <h1>Tasks</h1>
  <div class="container">
  <form @submit.prevent="createTask">
    <div class="mb-3">
      <label for="name" class="form-label">Name</label>
      <input type="text" id="name" v-model="task.name" aria-describedby="nameHelp">
      <div id="nameHelp" class="form-text">Please add a task name</div>
    </div>
    <div class="mb-3">
      <label for="dueDate" class="form-label">Due Date</label>
      <input type="datetime-local"  id="dueDate" v-model="task.dueDate">
    </div>
    <button type="submit" class="btn btn-success">
      Create
    </button>`
  </form>
  </div>
</template>
<script>
export default {
  data() {
    return {
      task: {
        name: '',
        dueDate: ''
      }
    }
  },
  methods: {
    createTask() {
      this.$emit('create-task', this.task);
      this.task = {
        name: '',
        dueDate: ''
      }
    }
  }
}
</script>