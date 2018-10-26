#pragma once
#include <pistache/endpoint.h>
#include <pistache/router.h>
#include <pistache/http.h>
#include <pistache/net.h>
#include <iostream>
#include <pqxx/pqxx>
#include <string>
#include "ConnectionPool.hpp"

namespace pr = Pistache::Rest;
namespace ph = Pistache::Http;


struct RequestHandler
{
    ConnectionPool pool;
    void connectionSetUp(pqxx::lazyconnection &con);
    void CreateUser(const pr::Request& rq, ph::ResponseWriter rw);
    void setRoutes(pr::Router& r);
};

