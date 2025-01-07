import React from "react";
import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import MainPage from "./pages/MainPage";
import Layout from "./pages/layout/Layout";
import UserComponent from "./pages/user/UserComponent";
import CardComponent from "./pages/card/CardComponent";
import CommunityComponent from "./pages/community/CommunityComponent";
import NoticeComponent from "./pages/notice/NoticeComponent";

export const drawerWidth = 240;
export const headerHeight = 78;

function App() {
  return (
    <Router>
      <Layout>
        <Routes>
          <Route path="/" element={<MainPage />} />
          <Route path="/user" element={<UserComponent />} />
          <Route path="/card" element={<CardComponent />} />
          <Route path="/community" element={<CommunityComponent />} />
          <Route path="/notice" element={<NoticeComponent />} />
        </Routes>
      </Layout>
    </Router>
  );
}

export default App;
