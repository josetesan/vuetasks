<template>
  <div v-if="tasks">
    <div v-for="task in tasks" :key="task.id">
        Task {{ task.id }}
        <blockquote>
          <p>{{ task.name }}</p>
          <footer>{{ task.dueDate }}</footer>
        </blockquote>
        <button @click="$emit('deleteTask', task)">Delete</button>
    </div>
  </div>
  <div v-else>
    Still loading ..
  </div>
</template>

<script>
export default {
  name: 'Tasks',
  data() {
    return {
      tasks: []
    }
  },
  mounted() {
    fetch('/api/tasks',{
      method: 'get',
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*'
      }
    }).then( res => {
      return res.json()
    }).then( json => {
       tasks.value = json
    })
  }
}
</script>

<style scoped>
h3 {
  margin: 40px 0 0;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

a {
  color: #42b983;
}
</style>
