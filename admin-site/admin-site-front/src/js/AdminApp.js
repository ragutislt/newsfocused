import logo from './../img/logo.svg';
import '../css/AdminApp.css';
import AdminAppLogin from './auth/AdminAppLogin';
import { useState } from 'react';
import UserTable from './users/UserTable';

function AdminApp() {
  const [loggedIn, setLoggedIn] = useState(false);

  // TODO do a preflight call to know if we are already authenticated and set the session accordingly

  if (!loggedIn) {
    return <AdminAppLogin onLogin={() => setLoggedIn(true)} />
  } else {
    return (
      // Add a header component
      <UserTable />
      // Add a footer component
    );
  }
}

export default AdminApp;
