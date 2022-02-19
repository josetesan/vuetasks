<template>
  <h1>Tasks</h1>
  <form @submit.prevent="createTask">
      <label for="name" >Name</label>
      <input type="text" id="name" v-model="task.name" aria-describedby="nameHelp">
      <div id="nameHelp">Please add a task name</div>
      <label for="dueDate" >Due Date</label>
      <input type="datetime-local"  id="dueDate" v-model="task.dueDate">
      <button>Create</button>
  </form>
</template>
<script>
export default {
  name: 'App',
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
<style>
h1 {
  border-bottom: 1px solid #ddd;
  display: inline-block;
  padding-bottom: 10px;
}
button {
  background: #0faf87;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  font-size: 16px;
  letter-spacing: 1px;
  cursor: pointer;
  margin: 10px;
}
form {
  max-width: 420px;
  margin: 30px auto;
  background: white;
  text-align: left;
  padding: 40px;
  border-radius: 10px;
}
label {
  color: #aaa;
  display: inline-block;
  margin: 25px 0 15px;
  font-size: 0.6em;
  text-transform: uppercase;
  letter-spacing: 1px;
  font-weight: bold;
}
input {
  display: block;
  padding: 10px 6px;
  width: 100%;
  box-sizing: border-box;
  border: none;
  border-bottom: 1px solid #ddd;
  color: #555;
}

</style>