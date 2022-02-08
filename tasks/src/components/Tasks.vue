<template>
  <div>
    <h1>Tasks</h1>
    <table class="table table-hover" v-if="!loading && data && data.length">
      <thead>
      <tr>
      <th scope="col">ID</th>
      <th scope="col">NAME</th>
      <th scope="col">DUE DATE</th>
      </tr>
      </thead>
      <tbody>
        <tr v-for="task in data" v-bind:key="task.id">
        <td><input type="checkbox" value="{{ task.id }}" v-model="checkedTask" /></td>
        <td>{{ task.name }}</td>
        <td>{{ task.dueDate }}</td>
      </tr>
      </tbody>
    </table>
    <p v-if="loading">
      Still loading ..
    </p>
    <p v-if="error">
      {{ error }}
    </p>
  </div>
</template>



<script>
import {onMounted, ref} from "vue";

export default {
  name: 'Tasks',
  props: {
  },
  setup() {
    const data = ref(null);
    const loading = ref(true);
    const error = ref(null);

    function fetchData() {
      loading.value = true;
      return fetch('http://localhost/api/tasks', {
        method: 'get',
        headers: {
          'Content-Type': 'application/json',
          'Access-Control-Allow-Origin': '*'
        }
      }).then(res => {
        if (!res.ok) {
          const error = new Error(res.statusText);
          error.json = res.json();
          throw error;
        }
        return res.json();
      }).then(json => {
        data.value = json;
      }).catch(err => {
        console.log(err);
        error.value = err;
        if (err.json) {
          return err.json.then(json => {
            error.value.message = json.message;
          });
        }
      }).then(() => {
        loading.value = false;
      });
    }

    onMounted(() => {
      fetchData();
    });

    return {
      data,
      loading,
      error
    };
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
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
