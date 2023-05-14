declare namespace ApiCommon {
  interface PageResult<T = any> {
    pageNo: number,
    pageSize: number,
    list: T,
    total: number,
  }
}

declare namespace ApiAuth {
  interface Token {
    token: string;
    refreshToken: string;
  }

  type UserInfo = Auth.UserInfo;
}

declare namespace ApiRoute {
  interface Route {
    routes: AuthRoute.Route[];
    home: AuthRoute.AllRouteKey;
  }
}

declare namespace ApiForm {
  interface Form {
    config: string;
    id: string;
    name: string;
    status: '1' | '0';
    created: string;
  }
}

declare namespace ApiUserManagement {

  interface Address {
    city?: string,
    state?: string,
    country?: string,
    zipCode?: string,
    detail?: string,
  }

  interface User {
    id: string,
    name: string,
    verified: boolean,
    created: string,
    lastSignIn: string,
    birthDay: string,
    gender: '0' | '1' | '2';
    phone: string;
    email: string;
    userStatus: '1' | '2' | '4';
    avatar: string
    address: Address
  }
}

declare namespace ApiChatManagement {
  interface message {
    id: string,
    text: string,
    timestamp: string,
    image?: string,
    user: {
      avatar: string,
      id: string
    }
  }
}

declare namespace ApiChatManagement {
  interface message {
    id: string,
    text: string,
    timestamp: string,
    image?: string,
    user: {
      avatar: string,
      id: string
    }
  }
}

declare namespace ApiChatManagement {
  interface message {
    id: string,
    text: string,
    timestamp: string,
    image?: string,
    user: {
      avatar: string,
      id: string
    }
  }
}
