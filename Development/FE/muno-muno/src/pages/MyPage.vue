<template>
  <div class="mypage">
    <HeaderB title="마이페이지" @back="goBack" />

    <!-- 프로필 섹션 -->
    <div class="profile-section">
      <div class="profile-image-container">
        <img
          :src="profileUrl ? profileUrl : require('@/assets/profile.png')"
          alt="프로필 이미지"
          class="profile-image"
        />
        <button class="edit-icon" @click="showProfileModal">
          <img src="@/assets/edit.svg" alt="프로필 수정" />
        </button>
      </div>

      <!-- 사용자 정보 섹션 -->
      <div class="user-info">
        <div class="user-detail">
          <span class="user-q">닉네임</span>
          <span class="user-a">{{
            user.nickname || '닉네임을 등록해주세요'
          }}</span>
        </div>
        <div class="user-detail">
          <span class="user-q">생년월일</span>
          <span class="user-a">{{
            user.birthDate || '생년월일을 등록해주세요'
          }}</span>
        </div>
        <div class="user-detail">
          <span class="user-q">휴대폰 번호</span>
          <span class="user-a">{{
            user.phoneNumber || '휴대폰 번호를 등록해주세요'
          }}</span>
        </div>
        <div class="user-detail">
          <span class="user-q">이메일 주소</span>
          <span class="user-a">{{
            user.email || '이메일 주소를 등록해주세요'
          }}</span>
        </div>
      </div>
    </div>

    <!-- 프로필 수정 버튼 -->
    <ButtonA class="profile-button" @click="goToEditPage">프로필 수정</ButtonA>

    <!-- 프로필 모달 컴포넌트 -->
    <ProfileModal
      v-if="isProfileModalVisible"
      @close="profileModalClose"
      :isVisible="isProfileModalVisible"
    />
  </div>
</template>

<script>
import HeaderB from '@/components/HeaderB.vue';
import ButtonA from '@/components/ButtonA.vue';
import { useUserStore } from '@/stores/userStore';
import { mapActions, mapState } from 'pinia';
import apiClient from '@/axios';
import ProfileModal from '@/components/ProfileModal.vue';

export default {
  name: 'MyPage',
  components: {
    HeaderB,
    ButtonA,
    ProfileModal,
  },
  computed: {
    ...mapState(useUserStore, ['userInfo']),
  },
  data() {
    return {
      user: {
        nickname: '',
        birthDate: '',
        phoneNumber: '',
        email: '',
      },
      profileUrl: '',
      isProfileModalVisible: false,
    };
  },
  methods: {
    ...mapActions(useUserStore, ['setUserInfo']),
    profileModalClose() {
      this.isProfileModalVisible = false;
      this.fetchUserProfile();
    },
    goBack() {
      this.$router.push('/');
    },
    // 프로필 수정 페이지로 이동
    goToEditPage() {
      this.$router.push({
        name: 'MyPageEdit',
      });
    },
    fetchUserProfile() {
      apiClient
        .get('/mypage/userInfo')
        .then((response) => {
          const data = response?.data?.data;
          this.user.email = data?.email;
          this.user.birthDate = data?.birth;
          this.user.nickname = data?.nickname;
          this.user.phoneNumber = data?.phone_number;
          this.profileUrl = data?.profile_url;
          this.setUserInfo({
            nickname: data?.nickname,
            profileUrl: data?.profile_url,
            salary: data?.salary,
            gender: data?.gender,
            birth: data?.birth,
          });
        })
        .catch((error) => {
          console.error('Error fetching user profile:', error);
        });
    },
    showProfileModal() {
      this.isProfileModalVisible = true;
    },
  },
  mounted() {
    this.fetchUserProfile();
  },
};
</script>

<style scoped>
.mypage {
  padding: 0 25px;
  background-color: var(--gr100);
  /* height: 726px; */
  overflow: hidden; /* 스크롤 비활성화 유지 */

  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 100vh; /* 뷰포트 전체 높이 사용 */
  position: fixed; /* 페이지 고정 */
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}

.profile-section {
  margin-top: 84px;
}

.profile-image-container {
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}

.profile-image {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  border: 0.5px solid var(--gr80);
  box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.06);
}

.edit-icon {
  position: absolute;
  bottom: -2px;
  right: 105px;
  border: none;
  cursor: pointer;
  background-color: transparent;
}

.user-info {
  margin-top: 20px;
}

.user-detail {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 0;
}

.user-q {
  color: var(--gr20);
  font-size: 16px;
  font-weight: 600;
}

.user-a {
  color: var(--gr50);
  font-size: 16px;
  font-weight: 400;
  text-align: right;
}
</style>
