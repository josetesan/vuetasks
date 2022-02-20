<template>
  <div id="app" class="container">
      <NewTask/>
      <Tasks/>
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
