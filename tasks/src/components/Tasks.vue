<template>
  <div v-if="tasks">
    <div v-for="task in tasks" :key="task.id">
        Task {{ task.id }}
        <blockquote>
          <p>{{ task.name }}</p>
          <footer>{{ task.dueDate }}</footer>
        </blockquote>
        <button @click="handleDelete(task.id)">Delete</button>
    </div>
  </div>
  <div v-else>
    Still loading ..
  </div>
  <div v-if="error">  
    <p class="error">{{ error }}</p>
  </div>
</template>

<script>

export default {
  name: 'Tasks',
  data() {
    return {
      tasks: [],
      error : ''
    };
  },
  mounted() {
    fetch('/api/tasks',{
      method: 'get',
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*'
      }
    }).then( async res =>  await res.json())
      .then( json => this.tasks = json)
      .catch( err => {
        this.error = err;
        console.log(err);
      })
  },
  methods: {
    handleDelete(id) {
      fetch(`/api/tasks/${id}`, {
          method: "DELETE"
      }).then( () => {
         this.tasks = this.tasks.filter(task => { return task.id !== id })
      })
    }
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

error {
  color: red;
}
</style>
