<template>
  <form>
    <div class="mb-3">
      <label for="name" class="form-label">Name</label>
      <input type="text" id="name" v-model="name" aria-describedby="nameHelp">
      <div id="nameHelp" class="form-text">Please add a task name</div>
    </div>
    <div class="mb-3">
      <label for="dueDate" class="form-label">Due Date</label>
      <input type="datetime-local"  id="dueDate" v-model="dueDate">
    </div>
    <button type="submit" @click="save()" class="btn btn-success">
      Create
    </button>`
  </form>

</template>

<script>



</script>