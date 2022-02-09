<template>
  <div id="app" class="container">
    <NewTask @create-task="postTask"/>
    <Tasks @delete-task="deleteTask"/>
  </div>
</template>

<script>
import NewTask from './components/NewTask.vue'
import Tasks from './components/Tasks.vue'

export default {
  name: 'App',
  components: {
    NewTask,
    Tasks
  },
  methods: {
    async postTask(task) {
      try {
        await fetch('/api/tasks', {
          method: 'POST',
          body: JSON.stringify(task),
          headers: { 'Content-type': 'application/json; charset=UTF-8' },
        });
      } catch (error) {
        console.error(error);
      }
    },
    async deleteTask(task) {
      try {
        await fetch(`/api/tasks/${task.id}`, {
          method: "DELETE"
        });
      } catch (error) {
        console.error(error);
      }
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
