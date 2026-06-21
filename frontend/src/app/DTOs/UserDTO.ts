export interface UserDTO {
    Username: string;
    Password: string;
  }

export function createUserDTO(Username: string, Password: string): UserDTO {
    return {Username, Password};
  }