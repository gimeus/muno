<template>
  <div class="container">
    <headerX title="회원가입" @close="handleClose" />
    <div class="input-container" style="margin-top: 80px">
      <div class="input-title">닉네임</div>
      <input
        type="text"
        class="input-long"
        placeholder="닉네임 입력"
        v-model="nickname"
        @blur="nicknameCheck"
      />
      <span v-if="nicknameError" class="error-text2"
        >이미 사용중인 닉네임입니다.</span
      >
      <span v-if="!nicknameError && nickname !== ''" class="correct-text2"
        >사용 가능한 닉네임입니다.</span
      >
    </div>
    <div class="input-container">
      <div class="input-title">이메일 인증</div>
      <div class="input-flex">
        <input
          type="email"
          class="input-short"
          placeholder="이메일 입력"
          v-model="email"
          @blur="emailCheck"
        />
        <button
          type="button"
          class="certification-btn"
          @click="startTimer"
          :disabled="emailError || email === ''"
        >
          {{ buttonText }}
        </button>
      </div>
      <div class="input-flex">
        <input
          type="text"
          class="input-short"
          placeholder="인증번호 입력"
          v-model="authcode"
          :disabled="!authcodeSended"
        />
        <button
          v-if="authcodeSended"
          type="button"
          class="certification-btn"
          @click="checkAuthCode"
          :disabled="authCheckBtnDisabled"
        >
          인증 확인
        </button>
      </div>
      <span v-if="emailError" class="error-text"
        >이미 사용중인 이메일입니다.</span
      >
      <span
        v-if="!emailError && email !== '' && !authcodeChecked"
        class="correct-text"
        >사용 가능한 이메일입니다.</span
      >
      <span v-if="authcodeChecked" class="correct-text"
        >인증 완료되었습니다.</span
      >
    </div>
    <div class="input-container">
      <div class="input-title">비밀번호</div>
      <input
        type="password"
        class="input-long"
        placeholder="비밀번호 입력"
        v-model="password"
      />
      <input
        type="password"
        class="input-long"
        placeholder="비밀번호 확인"
        v-model="passwordCheck"
      />
      <span v-if="passwordError" class="error-text"
        >비밀번호가 일치하지 않습니다.</span
      >
    </div>

    <div class="input-container">
      <div class="input-title">휴대폰 번호</div>
      <input
        type="text"
        class="input-long"
        placeholder="010-1234-5678"
        v-model="phoneNum"
        @input="formatphoneNum"
        maxlength="13"
      />
    </div>
    <div class="input-container">
      <div class="input-title">생년월일</div>
      <input
        type="date"
        v-model="selectedDate"
        placeholder=""
        class="input-short"
      />
    </div>
    <buttonA :disabled="isDisabled" @onClick="signUp"> 가입하기 </buttonA>
  </div>
</template>
<script>
import apiClient from '@/axios';
import headerX from '@/components/HeaderX.vue';
import buttonA from '@/components/ButtonA.vue';
export default {
  components: {
    headerX,
    buttonA,
  },
  data() {
    return {
      email: '',
      authcode: '',
      password: '',
      passwordCheck: '',
      nickname: '',
      phoneNum: '',
      selectedDate: '',
      emailError: false,
      nicknameError: false,
      authcodeSended: false,
      authcodeChecked: false,
      isTimerRunning: false,
      timeLeft: 180,
    };
  },
  computed: {
    authSendBtnDisabled() {
      return this.emailError;
    },
    authCheckBtnDisabled() {
      return this.authcodeChecked;
    },
    isDisabled() {
      // 모든 필드가 채워져 있는지 확인
      return (
        this.email === '' ||
        this.authcode === '' ||
        this.password === '' ||
        this.passwordCheck === '' ||
        this.nickname === '' ||
        this.phoneNum === '' ||
        this.selectedDate === '' ||
        this.passwordError || // 비밀번호 확인 오류가 있는 경우 비활성화
        this.emailError ||
        this.nicknameError
      );
    },
    passwordError() {
      // 비밀번호와 비밀번호 확인이 일치하는지 확인
      return (
        this.password !== '' &&
        this.passwordCheck !== '' &&
        this.password !== this.passwordCheck
      );
    },
    buttonText() {
      return this.isTimerRunning
        ? `${Math.floor(this.timeLeft / 60)}:${this.timeLeft % 60}`
        : '인증번호';
    },
  },
  methods: {
    handleClose() {
      this.$router.push({ name: 'Login' });
    },
    getAuthCode() {
      apiClient
        .post('/auth/sendAuthCode', {
          email: this.email,
          isForSignUp: true,
        })
        .catch((err) => {
          console.log(err);
        });
    },
    checkAuthCode() {
      const params = {
        email: this.email,
        authCode: this.authcode,
      };
      apiClient
        .get('/auth/checkAuthCode', { params })
        .then((res) => {
          if (res.status == 200) {
            this.authcodeChecked = true;
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    signUp() {
      apiClient
        .post('/auth/signUp', {
          nickname: this.nickname,
          email: this.email,
          phone_number: this.phoneNum,
          password: this.password,
          birth: this.selectedDate,
        })
        .then((res) => {
          if (res.status == 200) {
            console.log(res.data);
            this.$router.push('/login');
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    formatphoneNum() {
      // 입력 형식: 000-0000-0000
      let phoneNum = this.phoneNum.replace(/[^0-9]/g, ''); // 숫자만 남기기
      if (phoneNum.length > 3 && phoneNum.length <= 7) {
        phoneNum = `${phoneNum.slice(0, 3)}-${phoneNum.slice(3)}`;
      } else if (phoneNum.length > 7) {
        phoneNum = `${phoneNum.slice(0, 3)}-${phoneNum.slice(
          3,
          7
        )}-${phoneNum.slice(7, 11)}`;
      }
      this.phoneNum = phoneNum; // 포맷된 전화번호로 업데이트
    },
    emailCheck() {
      const params = { email: this.email };
      apiClient
        .get('/auth/checkEmail', {
          params,
        })
        .then((res) => {
          this.emailError = res.data['code'] == 422;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    nicknameCheck() {
      const params = { nickname: this.nickname };
      apiClient
        .get('/auth/checkNickname', {
          params,
        })
        .then((res) => {
          this.nicknameError = res.data['code'] == 422;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    async startTimer() {
      const params = { email: this.email };
      try {
        const res = await apiClient.get('/auth/checkEmail', { params });
        this.emailError = res.data['code'] == 422;

        // API 요청이 완료된 후 실행할 코드
      } catch (error) {
        console.error('API 요청 중 오류 발생:', error);
      }
      if (this.emailError) return;
      if (!this.isTimerRunning) {
        this.isTimerRunning = true;
        this.timeLeft = 180;
        this.authcodeSended = true;
        this.getAuthCode();
        const timer = setInterval(() => {
          if (this.timeLeft > 0) {
            this.timeLeft--;
          } else {
            clearInterval(timer);
            this.isTimerRunning = false;
          }
        }, 1000);
      }
    },
  },
};
</script>
<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  height: 100vh;
  box-sizing: border-box;
}
.input-container {
  width: 335px;
  flex-shrink: 0;
  margin-bottom: 20px;
  position: relative;
}
.input-title {
  color: var(--gray-40, #616b79);
  font-family: Pretendard;
  font-size: 14px;
  font-style: normal;
  font-weight: 600;
  line-height: 100%; /* 14px */
  margin-bottom: 8px;
}

.input-long {
  width: 335px;
  height: 56px;
  box-sizing: border-box;
  background: #ffffff;
  border: 0.5px solid #d6dae0;
  border-radius: 12px;
  padding: 0 0 0 20px; /* 텍스트 입력 시 패딩 추가 */
  font-family: 'Pretendard', sans-serif;
  font-size: 16px;
  color: #000000; /* 입력 텍스트 색상 */
  margin-bottom: 12px; /* 입력 필드 간 간격 추가 */
}

.certification-btn {
  width: 93px;
  height: 56px;
  background: #3e8aff;
  border-radius: 12px;
  border: none; /* 버튼 기본 테두리 제거 */
  cursor: pointer; /* 마우스를 올렸을 때 포인터 모양 변경 */
  font-family: 'Pretendard', sans-serif;
  font-style: normal;
  font-weight: 500px;
  font-size: 16px;
  color: #ffffff;
  display: flex;
  justify-content: center; /* 수평 중앙 정렬 */
  align-items: center; /* 수직 중앙 정렬 */
  text-align: center;
  margin-bottom: 12px; /* 입력 필드 간 간격 추가 */
}
.certification-btn:disabled {
  width: 93px;
  height: 56px;
  background: #cce0ff;
  border-radius: 12px;
  border: none; /* 버튼 기본 테두리 제거 */
  cursor: pointer; /* 마우스를 올렸을 때 포인터 모양 변경 */
  font-family: 'Pretendard', sans-serif;
  font-style: normal;
  font-weight: 500px;
  font-size: 16px;
  color: #ffffff;
  display: flex;
  justify-content: center; /* 수평 중앙 정렬 */
  align-items: center; /* 수직 중앙 정렬 */
  text-align: center;
  margin-bottom: 12px; /* 입력 필드 간 간격 추가 */
}

.input-short {
  width: 232px;
  height: 56px;
  flex-shrink: 0;
  border-radius: 12px;
  border: 0.5px solid var(--gray-60, #d6dae0);
  background: var(--gray-100, #fff);
  padding: 0 0 0 20px; /* 텍스트 입력 시 패딩 추가 */
  font-family: 'Pretendard', sans-serif;
  font-size: 16px;
  color: #000000; /* 입력 텍스트 색상 */
  margin-bottom: 12px; /* 입력 필드 간 간격 추가 */
}

.input-flex {
  display: flex;
  align-items: center; /* 세로 정렬 */
  justify-content: space-between; /* 양 끝으로 요소 배치 */
}
.button-container {
  margin-top: 30px;
}
.error-text {
  color: #f00;
  padding-left: 5px;
  font-family: Pretendard;
  font-style: normal;
  font-weight: 400;
  line-height: 100%; /* 12px */
  font-size: 12px;
  position: absolute; /* 위치를 절대적으로 설정하여 요소가 레이아웃을 밀지 않음 */
  top: 90%; /* 입력 필드 바로 아래에 위치 */
  left: 0;
  margin-top: 10px;
}
.error-text2 {
  color: #f00;
  padding-left: 5px;
  font-family: Pretendard;
  font-style: normal;
  font-weight: 400;
  line-height: 100%; /* 12px */
  font-size: 12px;
  position: absolute; /* 위치를 절대적으로 설정하여 요소가 레이아웃을 밀지 않음 */
  top: 90%; /* 입력 필드 바로 아래에 위치 */
  left: 0;
  margin-top: 5px;
}
.correct-text {
  color: rgb(8, 0, 255);
  padding-left: 5px;
  font-family: Pretendard;
  font-style: normal;
  font-weight: 400;
  line-height: 100%; /* 12px */
  font-size: 12px;
  position: absolute; /* 위치를 절대적으로 설정하여 요소가 레이아웃을 밀지 않음 */
  top: 90%; /* 입력 필드 바로 아래에 위치 */
  left: 0;
  margin-top: 10px;
}

.correct-text2 {
  color: rgb(8, 0, 255);
  padding-left: 5px;
  font-family: Pretendard;
  font-style: normal;
  font-weight: 400;
  line-height: 100%; /* 12px */
  font-size: 12px;
  position: absolute; /* 위치를 절대적으로 설정하여 요소가 레이아웃을 밀지 않음 */
  top: 90%; /* 입력 필드 바로 아래에 위치 */
  left: 0;
  margin-top: 5px;
}
</style>
