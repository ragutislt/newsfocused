import logo from './../img/logo.svg';
import './../css/AdminApp.css';
import AdminAppLogin from './auth/AdminAppLogin';

function AdminApp() {
  const loggedIn = false;

  if (!loggedIn) {
    return <AdminAppLogin />
  } else {
    return (
      <div className="AdminApp">
        <header className="AdminApp-header">
          <img src={logo} className="AdminApp-logo" alt="logo" />
          <p>
            Edit <code>src/App.js</code> and save to reload.
          </p>
          <a
            className="AdminApp-link"
            href="https://reactjs.org"
            target="_blank"
            rel="noopener noreferrer"
          >
            Learn React
          </a>
        </header>
      </div>
    );
  }
}

export default AdminApp;
