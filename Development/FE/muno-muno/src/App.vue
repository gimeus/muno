<template>
  <div id="app">
    <!-- <StatusBar /> -->
    <div class="content">
      <router-view></router-view>
    </div>
    <TabBar v-if="showTabBar" />
  </div>
</template>

<script>
import TabBar from './components/TabBar.vue';
// import StatusBar from './components/StatusBar.vue';
export default {
  components: {
    TabBar,
    // StatusBar,
  },
  data() {
    return {
      hideRoutes: [
        '/login',
        '/signup',
        '/mypage',
        '/mypageedit',
        '/search',
        '/message',
        '/findPassword',
        '/mydata',
        '/incomesetting',
        '/limitsetting',
        '/community/notification',
        '/community/create',
        '/community/modify',
      ],
    };
  },
  computed: {
    showTabBar() {
      if (/^\/community\/\d+$/.test(this.$route.path)) return false;
      if (this.$route.path.includes('/community/modify')) return false;
      if (this.$route.path.includes('/message/')) return false;
      return !this.hideRoutes.includes(this.$route.path);
    },
  },
};
</script>
<style>
body,
html {
  font-family: 'Pretendard', sans-serif;
  /* width: 375px;
  height: 768px; */
  margin: 0;
  padding: 0;
  overflow: auto;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

#app {
  width: 375px;
  height: 812px;
  font-family: 'Pretendard', sans-serif;
}

:root {
  --gr10: #1f2226;
  --gr20: #292d33;
  --gr30: #383e47;
  --gr40: #616b79;
  --gr50: #8892a0;
  --gr60: #c4cad4;
  --gr70: #edeff2;
  --gr80: #f5f6f7;
  --gr90: #fbfbfc;
  --gr100: #ffffff;
  --p10: #3e8aff;
  --p20: #5398ff;
  --p30: #97c0fe;
  --p40: #99c1ff;
  --p50: #b2d1ff;
  --p60: #b2d1ff;
  --p70: #ebf3ff;
  --p80: #f0f6ff;
}

.content {
  /* padding-top: 44px; */
  height: calc(100% - 86px);
  overflow-y: auto;
}

.content::-webkit-scrollbar,
html::-webkit-scrollbar,
body::-webkit-scrollbar {
  width: 0;
  display: none;
}
</style>
